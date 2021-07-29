/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import tool.compet.core4j.BuildConfig;
import tool.compet.core4j.DkLogs;

public class OwnSchedulerInObserver<M> extends DkControllable<M> {
	final DkScheduler<M> scheduler;
	Callable<M> task;

	public OwnSchedulerInObserver(DkObserver<M> child, DkScheduler<M> scheduler) {
		super(child);
		this.scheduler = scheduler;
	}

	void start(DkObservableSource<M> parent, long delay, TimeUnit timeUnit, boolean isSerial) throws Exception {
		// Give to children a chance to cancel scheduling in other thread
		child.onSubscribe(this);
		if (isCancel) {
			isCanceled = true;
			onFinal();
			return;
		}

		// Task in the service (IO thread...)
		task = () -> {
			// maybe it takes long time to schedule, so check again cancel event from user
			if (isCancel) {
				isCanceled = true;
				onFinal();
			}
			else {
				parent.subscribe(OwnSchedulerInObserver.this);
			}
			return null;
		};

		// Start subscribe in other thread
		scheduler.schedule(task, delay, timeUnit, isSerial);
	}

	@Override
	public boolean cancel(boolean mayInterruptThread) {
		boolean ok = super.cancel(mayInterruptThread);

		if (task != null) {
			ok |= scheduler.cancel(task, mayInterruptThread);
		}

		isCanceled = ok;

		if (BuildConfig.DEBUG) {
			DkLogs.info(this, "Cancel task: " + task + ", ok: " + ok);
		}

		return ok;
	}
}