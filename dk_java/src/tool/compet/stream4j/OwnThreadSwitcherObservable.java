/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import tool.compet.core4j.DkRunnable1;

/**
 * Normally, switch thread, run in main thread instead of IO thread.
 */
public class OwnThreadSwitcherObservable<M> extends TheObservableSourceImpl<M> {
	private final DkScheduler<M> scheduler;
	@Nullable private final DkRunnable1<M> action;
	private final long delay;
	private final TimeUnit timeUnit;
	private final boolean isSerial;

	public OwnThreadSwitcherObservable(DkObservableSource<M> parent,
		DkScheduler<M> scheduler,
		@Nullable DkRunnable1<M> action,
		long delay, TimeUnit timeUnit,
		boolean isSerial
	) {
		this.parent = parent;
		this.scheduler = scheduler;
		this.action = action;
		this.delay = delay;
		this.timeUnit = timeUnit;
		this.isSerial = isSerial;
	}

	@Override
	public void subscribeActual(DkObserver<M> child) throws Exception {
		parent.subscribe(new OwnThreadSwitcherObserver<>(child, scheduler, action, delay, timeUnit, isSerial));
	}
}
