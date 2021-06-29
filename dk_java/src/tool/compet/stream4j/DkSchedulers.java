/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkExecutorService;
import tool.compet.stream.MyUiScheduler;

@SuppressWarnings("unchecked")
public class DkSchedulers {
	private static DkScheduler ioScheduler;
	private static DkScheduler uiScheduler;

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

	// Android ui thread scheduler
	public static <T> DkScheduler<T> ui() {
		if (uiScheduler == null) {
			synchronized (DkSchedulers.class) {
				if (uiScheduler == null) {
					uiScheduler = new MyUiScheduler<>();
				}
			}
		}
		return (DkScheduler<T>) uiScheduler;
	}
}
