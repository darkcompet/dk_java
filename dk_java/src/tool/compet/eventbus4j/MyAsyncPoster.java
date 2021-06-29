/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.eventbus4j;

import tool.compet.core4j.DkConsoleLogs;
import tool.compet.core4j.DkExecutorService;

import java.util.concurrent.ScheduledExecutorService;

class MyAsyncPoster {
	private final SerialExecutor serialExecutor;

	MyAsyncPoster(DkEventBus eventbus) {
		serialExecutor = new SerialExecutor(DkExecutorService.getExecutor(), eventbus);
	}

	void post(DkEventBus eventbus, MySubscription subscription, Object event) {
		DkExecutorService.getExecutor().execute(() -> {
			eventbus.invokeSubscriber(subscription, event);
		});
	}

	void enqueue(MySubscription subscription, Object eventData) {
		serialExecutor.execute(new MyPendingPost(subscription, eventData));
	}

	static class SerialExecutor {
		final ScheduledExecutorService service;
		final MyPendingPostQueue queue;
		final DkEventBus eventbus;
		MyPendingPost active;

		SerialExecutor(ScheduledExecutorService executor, DkEventBus eventbus) {
			this.service = executor;
			this.queue = new MyPendingPostQueue();
			this.eventbus = eventbus;
		}

		synchronized void execute(MyPendingPost pp) {
			queue.enqueue(pp);
			// start schedule if have not active task
			if (active == null) {
				executeNext();
			}
		}

		synchronized void executeNext() {
			if ((active = queue.dequeue()) == null) {
				return;
			}

			service.execute(() -> {
				try {
					eventbus.invokeSubscriber(active);
				}
				catch (Exception e) {
					DkConsoleLogs.warning(this, "Error occured when run task on serial-executor: " + active);
					DkConsoleLogs.error(this, e);
				}
				finally {
					executeNext();
				}
			});
		}
	}
}
