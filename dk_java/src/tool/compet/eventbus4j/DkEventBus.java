/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.eventbus4j;

import android.os.Looper;

import androidx.collection.ArrayMap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import tool.compet.core4j.DkConsoleLogs;
import tool.compet.core4j.DkIntObjectArrayMap;
import tool.compet.reflection4j.DkReflectionFinder;

/**
 * This library supports communication between poster and susbcribers (for eg,. Android Activity/Fragment).
 * You can post an object value to all consumers which interest your event, and then consumer can
 * handle that event in the thread that consumer specified.
 * It also support #postSticky() when consumer was absent, but consumer must remove sticky event
 * manually when it consumes the event.
 * <p></p>
 * Here is usage example:
 * <pre>
 *    // In publisher: Post value to a topic with specified id
 *    DkEventBus.getIns().post(id, obj);
 *
 *    // In subscriber: Listen event at a topic which has own id. Note that, param must Not be primitive.
 *    #@DkSubscribe(id = XXX, allowNullParam = false)
 *    public void onEvent(Object param) {
 *    }
 * </pre>
 * <p></p>
 * <p>
 * Refer：https://github.com/greenrobot/EventBus
 */
public class DkEventBus {
	void foo() {
//		DkEventBus.getIns().observe(this, "abc", obj -> {});
//		DkEventBus.getIns().observe(this, "xyz", obj -> {}, option -> option.runOnMainThread().priority(10));
//
//		DkEventBus.getIns().post("abc", 1200);
//		DkEventBus.getIns().post("xyz", new DkEventModel<Integer>(1200));
	}

	private static DkEventBus INS;

	// Store subscriptions for each ID
	protected final DkIntObjectArrayMap<CopyOnWriteArrayList<OwnSubscription>> subscriptions;

	// Cache subscription methods for each Class to improve performance
	protected final ArrayMap<Class<?>, List<OwnSubscriptionMethod>> subscriptionMethodCache;

	// Store sticky events for each ID. Note that, it not be remove even the Class is unregistered.
	// So you should manually remove it via removeStickyEvents().
	protected final DkIntObjectArrayMap<List<Object>> stickyEvents;

	protected MyAsyncPoster asyncPoster;
	// Android main thread poster
	protected MyMainPoster mainPoster;

	protected DkEventBus() {
		this.subscriptions = new DkIntObjectArrayMap<>(64);
		this.subscriptionMethodCache = new ArrayMap<>(64);
		this.stickyEvents = new DkIntObjectArrayMap<>(64);
	}

	public static DkEventBus getIns() {
		if (INS == null) {
			synchronized (DkEventBus.class) {
				if (INS == null) {
					INS = new DkEventBus();
				}
			}
		}
		return INS;
	}

	/**
	 * Add newly all subscriptions of this target to bus system.
	 */
	public <T> void register(T subscriber) {
		Class subscriberClass = subscriber.getClass();

		// Lookup subscription methods of subscriber
		ArrayMap<Class<?>, List<OwnSubscriptionMethod>> methodCache = subscriptionMethodCache;
		List<OwnSubscriptionMethod> subscriptionMethods;

		synchronized (this.subscriptionMethodCache) {
			subscriptionMethods = methodCache.get(subscriberClass);
		}

		// Register methods of subcriber
		if (subscriptionMethods == null) {
			List<Method> methods = DkReflectionFinder.getIns().findMethods(subscriberClass, DkSubscribe.class);

			if (methods.size() > 0) {
				subscriptionMethods = new ArrayList<>();

				for (Method method : methods) {
					subscriptionMethods.add(new OwnSubscriptionMethod(method));
				}

				synchronized (this.subscriptionMethodCache) {
					if (! methodCache.containsKey(subscriberClass)) {
						methodCache.put(subscriberClass, subscriptionMethods);
					}
				}
			}
		}

		// Add newly subscription to subscriptions for each subscriptionMethod
		if (subscriptionMethods != null && subscriptionMethods.size() > 0) {
			synchronized (this) {
				CopyOnWriteArrayList<OwnSubscription> subscriptions;

				for (OwnSubscriptionMethod subscriptionMethod : subscriptionMethods) {
					final int id = subscriptionMethod.id;
					subscriptions = this.subscriptions.get(id);

					if (subscriptions == null) {
						subscriptions = new CopyOnWriteArrayList<>();
						this.subscriptions.put(subscriptionMethod.id, subscriptions);
					}

					OwnSubscription subscription = new OwnSubscription(subscriber, subscriptionMethod);
					binaryInsertionSynced(subscription, subscriptions);

					// Send sticky event to this subscription
					if (subscriptionMethod.sticky) {
						List<Object> stickyEvents = this.stickyEvents.get(id);

						if (stickyEvents != null && stickyEvents.size() > 0) {
							boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();

							for (Object event : stickyEvents) {
								sendEventDataToSubscription(subscription, event, isMainThread);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Remove all subscriptions of this target from bus system
	 */
	public synchronized <T> void unregister(T subscriber) {
		// For Class: remove from subscription methods
		subscriptionMethodCache.remove(subscriber.getClass());

		// For ID: remove from subscriptions
		DkIntObjectArrayMap<CopyOnWriteArrayList<OwnSubscription>> cache = subscriptions;

		for (int index = 0, N = cache.size(); index < N; ++index) {
			List<OwnSubscription> subscriptions = cache.valueAt(index);

			if (subscriptions != null) {
				for (int subIndex = subscriptions.size() - 1; subIndex >= 0; --subIndex) {
					OwnSubscription subscription = subscriptions.get(subIndex);

					if (subscriber == subscription.subscriber) {
						// make this subscription unactive
						subscription.active = false;
						subscriptions.remove(subIndex);
					}
				}
			}
		}
	}

	/**
	 * Check whether this subscriber was registered or not.
	 */
	public synchronized <T> boolean isRegistered(T subscriber) {
		DkIntObjectArrayMap<CopyOnWriteArrayList<OwnSubscription>> cache = subscriptions;

		for (int index = 0, N = cache.size(); index < N; ++index) {
			List<OwnSubscription> subscriptions = cache.valueAt(index);

			if (subscriptions != null) {
				for (int subIndex = subscriptions.size() - 1; subIndex >= 0; --subIndex) {
					OwnSubscription subscription = subscriptions.get(subIndex);

					if (subscriber == subscription.subscriber) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Check whether this Class was registered or not.
	 */
	public <T> boolean isRegistered(Class<T> subscriber) {
		List<OwnSubscriptionMethod> methods;

		synchronized (subscriptionMethodCache) {
			methods = this.subscriptionMethodCache.get(subscriber);
		}

		return methods != null && methods.size() > 0;
	}

	/**
	 * Notify all subscriptions that have same id.
	 */
	public <T> void post(int id, T data) {
		CopyOnWriteArrayList<OwnSubscription> subscriptions;

		synchronized (this.subscriptions) {
			subscriptions = this.subscriptions.get(id);
		}

		if (subscriptions != null) {
			boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();

			for (OwnSubscription subscription : subscriptions) {
				sendEventDataToSubscription(subscription, data, isMainThread);
			}
		}
	}

	/**
	 * Post to given target class.
	 */
	public <T> void post(int id, Class subscriber, T data) {
		CopyOnWriteArrayList<OwnSubscription> subscriptions;

		synchronized (this.subscriptions) {
			subscriptions = this.subscriptions.get(id);
		}

		if (subscriptions != null) {
			boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();

			for (OwnSubscription subscription : subscriptions) {
				if (subscriber.equals(subscription.subscriber.getClass())) {
					sendEventDataToSubscription(subscription, data, isMainThread);
					break;
				}
			}
		}
	}

	/**
	 * Post to targets that not be in excepted target classes.
	 */
	public <T> void postExcept(int id, T data, Class... exceptSubscriberClasses) {
		CopyOnWriteArrayList<OwnSubscription> subscriptions;

		synchronized (this.subscriptions) {
			subscriptions = this.subscriptions.get(id);
		}

		if (subscriptions != null) {
			List<Class> blacklist = Arrays.asList(exceptSubscriberClasses);
			boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();

			for (OwnSubscription subscription : subscriptions) {
				if (!blacklist.contains(subscription.subscriber.getClass())) {
					sendEventDataToSubscription(subscription, data, isMainThread);
				}
			}
		}
	}

	/**
	 * Post to targets that not be in excepted targets.
	 */
	public <T> void postExcept(int id, T data, Object... exceptSubscribers) {
		CopyOnWriteArrayList<OwnSubscription> subscriptions;

		synchronized (this.subscriptions) {
			subscriptions = this.subscriptions.get(id);
		}

		if (subscriptions != null) {
			List<Object> blacklist = Arrays.asList(exceptSubscribers);
			boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();

			for (OwnSubscription subscription : subscriptions) {
				if (!blacklist.contains(subscription.subscriber)) {
					sendEventDataToSubscription(subscription, data, isMainThread);
				}
			}
		}
	}

	/**
	 * Post sticky the event to all subscriptions that has same id.
	 * Note that, subscription should take care of removing this sticky event.
	 */
	public <T> void postSticky(int id, T event) {
		synchronized (stickyEvents) {
			List<Object> events = stickyEvents.get(id);

			if (events == null) {
				stickyEvents.put(id, (events = new ArrayList<>()));
			}

			events.add(event);
		}

		post(id, event);
	}

	/**
	 * @return sticky events that was assigned to this id.
	 */
	public List<Object> getStickyEvents(int id) {
		synchronized (stickyEvents) {
			return stickyEvents.get(id);
		}
	}

	/**
	 * Remove all sticky events that assigned to this id.
	 */
	public void removeStickyEvents(int id) {
		synchronized (stickyEvents) {
			stickyEvents.remove(id);
		}
	}

	/**
	 * Remove a sticky event that assigned to this id.
	 */
	public synchronized void removeStickyEvent(int id, Object event) {
		List<Object> events = stickyEvents.get(id);

		if (events != null) {
			events.remove(event);
		}
	}

	/**
	 * Remove all sticky events in bus system.
	 */
	public void removeAllStickyEvents() {
		synchronized (stickyEvents) {
			stickyEvents.clear();
		}
	}

	protected void invokeSubscriber(OwnPendingPost pp) {
		if (pp != null) {
			invokeSubscriber(pp.subscription, pp.event);
		}
	}

	protected <T> void invokeSubscriber(OwnSubscription subscription, T eventData) {
		if (subscription.active) {
			try {
				subscription.invoke(eventData);
			}
			catch (Exception e) {
				DkConsoleLogs.error(this, e);
			}
		}
	}

	protected void binaryInsertionSynced(OwnSubscription subscription, List<OwnSubscription> subscriptions) {
		final int N = subscriptions.size();
		if (N == 0) {
			subscriptions.add(subscription);
			return;
		}

		int target = subscription.subscriptionMethod.priority;
		int index = 0;
		int left = 0, right = N;

		while (left <= right) {
			int mid = (left + right) >> 1;
			int value = subscriptions.get(mid).subscriptionMethod.priority;

			if (target < value) {
				right = mid - 1;
				index = mid;
			}
			else if (target > value) {
				left = index = mid + 1;
			}
			else {
				index = mid;
				break;
			}
		}

		index = Math.min(N, index); // index <= N
		subscriptions.add(index, subscription);
	}

	protected MyAsyncPoster getAsyncPoster() {
		if (asyncPoster == null) {
			asyncPoster = new MyAsyncPoster(this);
		}
		return asyncPoster;
	}

	protected MyMainPoster getMainPoster() {
		if (mainPoster == null) {
			mainPoster = new MyMainPoster(this);
		}
		return mainPoster;
	}

	protected <T> void sendEventDataToSubscription(OwnSubscription subscription, T eventData, boolean isMainThread) {
		switch (subscription.subscriptionMethod.threadMode) {
			case DkThreadMode.POSTER: {
				invokeSubscriber(subscription, eventData);
				break;
			}
			case DkThreadMode.ASYNC: {
				getAsyncPoster().post(this, subscription, eventData);
				break;
			}
			case DkThreadMode.ASYNC_ORDERED: {
				getAsyncPoster().enqueue(subscription, eventData);
				break;
			}
			case DkThreadMode.ANDROID_MAIN: {
				if (isMainThread) {
					invokeSubscriber(subscription, eventData);
				}
				else {
					getMainPoster().enqueue(subscription, eventData);
				}
				break;
			}
			case DkThreadMode.ANDROID_MAIN_ORDERED: {
				getMainPoster().enqueue(subscription, eventData);
				break;
			}
		}
	}
}
