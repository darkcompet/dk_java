/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.eventbus4j;

import tool.compet.core4j.DkLogs;
import tool.compet.core4j.DkExecutorService;

import java.util.concurrent.ScheduledExecutorService;

class MyAsyncPoster {
	private final SerialExecutor serialExecutor;

	MyAsyncPoster(DkEventBus eventbus) {
		serialExecutor = new SerialExecutor(DkExecutorService.getExecutor(), eventbus);
	}

	void post(DkEventBus eventbus, OwnSubscription subscription, Object event) {
		DkExecutorService.getExecutor().execute(() -> {
			eventbus.invokeSubscriber(subscription, event);
		});
	}

	void enqueue(OwnSubscription subscription, Object eventData) {
		serialExecutor.execute(new OwnPendingPost(subscription, eventData));
	}

	static class SerialExecutor {
		final ScheduledExecutorService service;
		final OwnPendingPostQueue queue;
		final DkEventBus eventbus;
		OwnPendingPost active;

		SerialExecutor(ScheduledExecutorService executor, DkEventBus eventbus) {
			this.service = executor;
			this.queue = new OwnPendingPostQueue();
			this.eventbus = eventbus;
		}

		synchronized void execute(OwnPendingPost pp) {
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
					DkLogs.warning(this, "Error occured when run task on serial-executor: " + active);
					DkLogs.error(this, e);
				}
				finally {
					executeNext();
				}
			});
		}
	}
}
