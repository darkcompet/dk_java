/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import java.util.concurrent.TimeUnit;

/**
 * Normally, switch thread, and run code of upper nodes in IO (background) thread.
 */
public class OwnScheduleInObservable<M> extends DkObservable<M, OwnScheduleInObservable> {
	private final DkScheduler<M> scheduler;
	private final long delay;
	private final TimeUnit timeUnit;
	private final boolean isSerial;

	public OwnScheduleInObservable(DkObservableSource<M> parent,
		DkScheduler<M> scheduler,
		long delay, TimeUnit unit,
		boolean isSerial
	) {
		super(parent);
		this.scheduler = scheduler;
		this.delay = delay;
		this.timeUnit = unit;
		this.isSerial = isSerial;
	}

	@Override
	public void subscribeActual(DkObserver<M> child) throws Exception {
		OwnSchedulerInObserver<M> myObserver = new OwnSchedulerInObserver<>(child, this.scheduler);
		// From now, code will be scheduled in other thread
		myObserver.start(parent, delay, timeUnit, isSerial);
	}
}
