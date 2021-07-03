/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkExecutorService;

@SuppressWarnings("unchecked")
public class DkSchedulers {
	protected static DkScheduler ioScheduler;

	// Background thread scheduler
	public static <T> DkScheduler<T> io() {
		if (ioScheduler == null) {
			synchronized (DkSchedulers.class) {
				if (ioScheduler == null) {
					ioScheduler = new MyIoScheduler<>(DkExecutorService.getExecutor());
				}
			}
		}
		return (DkScheduler<T>) ioScheduler;
	}
}
