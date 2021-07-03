/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import tool.compet.core4j.DkCallable1;
import tool.compet.core4j.DkRunnable;
import tool.compet.core4j.DkRunnable1;

/**
 * This works like RxJava or Java stream.
 * Refer: https://github.com/ReactiveX/RxJava/
 *
 * M: model which be passed down to child node
 */
@SuppressWarnings("unchecked")
public abstract class DkObservable<M, T extends DkObservableSource> implements DkObservableSource<M> {
	protected DkObservableSource<M> parent;
	protected DkObservableSource<M> current;

	protected DkObservable() {
	}

	protected DkObservable(DkObservableSource<M> parent) {
		this.parent = parent;
	}
	
	// region Basic instance creator

	/**
	 * Its useful if you wanna customize emitting-logic like onNext(), onError()... in #DkObserver to children.
	 * Note that, you must implement logic to call #onFinal() in observer.
	 */
//	public static <M> DkObservable<M> fromEmitter(DkEmitter<M> emitter) {
//		return new MyEmitterObservable<M>(emitter);
//	}

	/**
	 * Make an execution without input, then pass result to lower node. Note that, you can cancel
	 * execution of running thread but cannot control (cancel, pause, resume...) it deeply.
	 * To overcome this, just use #withControllable() instead.
	 */
//	public static <M> DkObservable<M> fromCallable(DkCallable<M> execution) {
//		return new MyGodCallableObservable<>(execution);
//	}

	/**
	 * Its useful if you wanna control (pause, resume, cancel...) state of the task.
	 */
//	public static <M> DkObservable<M> fromControllable(DkControllable<M> task) {
//		return new MyGodControllableObservable<>(task);
//	}

	/**
	 * Use it if you just wanna send item to children.
	 */
//	public static <M, T extends TheObservable<M>> DkObservable<M, T> from(M item) {
//		return new MyGodArrayObservable<>(item);
//	}

	/**
	 * Use it if you just wanna send item to children.
	 */
//	public static <M> DkObservable<M> from(M[] items) {
//		return new MyGodArrayObservable<>(items);
//	}

	/**
	 * Use it if you just wanna send item to children.
	 */
//	public static <M> DkObservable<M> from(Iterable<M> items) {
//		return new MyGodIterableObservable<>(items);
//	}

	// endregion Basic instance creator

	public <N, TN extends DkObservableSource<N>> TN jmap(DkCallable1<M, N> function) {
		current = ((DkObservableSource<M>) new OwnMapObservable<>(current, function));
		return (TN) this;
	}

	public <N, TN extends DkObservableSource<N>> TN jmapStream(DkCallable1<M, DkObservableSource<N>> function) {
		current = ((DkObservableSource<M>) new OwnMapStreamObservable<>(current, function));
		return (TN) this;
	}

	public T ignoreError() {
		current = new OwnIgnoreErrorObservable<>(current);
		return (T) this;
	}

	public T scheduleInBackground() {
		return scheduleIn(DkSchedulers.io(), 0, TimeUnit.MILLISECONDS, false);
	}

	public T scheduleIn(DkScheduler<M> scheduler) {
		return scheduleIn(scheduler, 0, TimeUnit.MILLISECONDS, false);
	}

	public T scheduleIn(DkScheduler<M> scheduler, boolean isSerial) {
		return scheduleIn(scheduler, 0, TimeUnit.MILLISECONDS, isSerial);
	}

	public T scheduleIn(DkScheduler<M> scheduler, long delay, TimeUnit unit, boolean isSerial) {
		current = new OwnScheduleInObservable<>(current, scheduler, delay, unit, isSerial);
		return (T) this;
	}

	public T observeOn(DkScheduler<M> scheduler) {
		return observeOn(scheduler, 0L, TimeUnit.MILLISECONDS, true);
	}

	public T observeOn(DkScheduler<M> scheduler, long delayMillis) {
		return observeOn(scheduler, delayMillis, TimeUnit.MILLISECONDS, true);
	}

	public T observeOn(DkScheduler<M> scheduler, long delay, TimeUnit unit, boolean isSerial) {
		current = new OwnThreadSwitcherObservable<>(current, scheduler, null, delay, unit, isSerial);
		return (T) this;
	}

	public T switchThread(DkScheduler<M> scheduler, DkRunnable1<M> action) {
		return switchThread(scheduler, action, 0, TimeUnit.MILLISECONDS, true);
	}

	/**
	 * Make flow run in other thread, this is thread-switching.
	 */
	public T switchThread(DkScheduler<M> scheduler, DkRunnable1<M> action, long delay, TimeUnit unit, boolean isSerial) {
		current = new OwnThreadSwitcherObservable<>(current, scheduler, action, delay, unit, isSerial);
		return (T) this;
	}

	/**
	 * Hears subscribe-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public T doOnSubscribe(DkRunnable1<DkControllable> action) {
		current = new OwnOnSubscribeObservable<>(current, action);
		return (T) this;
	}

	/**
	 * Hears success-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public T doOnNext(DkRunnable1<M> action) {
		current = new OwnOnNextObservable<>(current, action);
		return (T) this;
	}

	/**
	 * Hears error-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public T doOnError(DkRunnable1<Throwable> action) {
		current = new OwnOnErrorObservable<>(current, action);
		return (T) this;
	}

	/**
	 * Hears complete-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public T doOnComplete(DkRunnable action) {
		current = new OwnOnCompleteObservable<>(current, action);
		return (T) this;
	}

	/**
	 * Hears final-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public T doOnFinal(DkRunnable action) {
		current = new OwnOnFinalObservable<>(current, action);
		return (T) this;
	}

	public void subscribeAsync() {
		this.scheduleInBackground().subscribe();
	}

	public DkControllable<M> subscribeForControllable() {
		return subscribeForControllable(new DkControllable<>(new OwnLeafObserver<>()));
	}

	/**
	 * Subscribe a observer (listener, callback) to stream, so we can listen what happening in stream.
	 * Differ with aother subscribe() method, this will return Controllable object,
	 * so you can control (dispose, resume, pause...) stream anytime you want.
	 */
	public DkControllable<M> subscribeForControllable(DkObserver<M> observer) {
		DkControllable<M> controllable = new DkControllable<>(observer);
		subscribe(controllable);
		return controllable;
	}

	/**
	 * Subscribe with empty (leaf) observer (listener, callback) to stream.
	 */
	@Override
	public void subscribe() {
		subscribe(new OwnLeafObserver<>());
	}

	/**
	 * Subscribe with an observer (listener, callback) to stream, we can listen what happen in the stream.
	 *
	 * @param observer For first time, this is object passed from caller. Next, we wrap it to own observer
	 *                 and pass up to parent node.
	 */
	@Override
	public void subscribe(@NonNull DkObserver<M> observer) {
		try {
			// Go up to send observer (normally wrapped observer) of this node to parent node,
			// by do it, we can make linked list of observers, so parent can pass events to this node later.
			if (current == null) { // current: lowest node, we are not in God node
				subscribeActual(observer);
			}
			else { // current: lowest node but we are in God node
				current.subscribeActual(observer);
				current = null;
			}
		}
		catch (Exception e) {
			// Unable to subscribe (make node-link process), just pass error and then final events
			// to child observer
			observer.onError(e);
			observer.onFinal();
		}
	}
}
