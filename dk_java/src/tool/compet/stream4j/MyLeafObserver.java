/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.BuildConfig;
import tool.compet.core4j.DkConsoleLogs;

/**
 * This is lowest (leaf) observer at first subscribe, so all events come to it will not be sent to down more.
 * From that, we can write log to observe events here.
 */
class MyLeafObserver<T> implements DkObserver<T> {
	MyLeafObserver() {
	}

	@Override
	public void onSubscribe(DkControllable controllable) {
		if (BuildConfig.DEBUG) {
			__startTime = System.currentTimeMillis();
		}
	}

	@Override
	public void onNext(T item) {
	}

	@Override
	public void onError(Throwable e) {
		if (BuildConfig.DEBUG) {
			DkConsoleLogs.error(this, e, "Stream error after %d (ms)", System.currentTimeMillis() - __startTime);
		}
	}

	@Override
	public void onComplete() {
		if (BuildConfig.DEBUG) {
			DkConsoleLogs.info(this, "Stream complete after %d (ms)", System.currentTimeMillis() - __startTime);
		}
	}

	@Override
	public void onFinal() {
		if (BuildConfig.DEBUG) {
			DkConsoleLogs.info(this, "Stream final after %d (ms)", System.currentTimeMillis() - __startTime);

			if (++__testFinalCount > 1) {
				DkConsoleLogs.warning(this, "Wrong implementation of #onFinal. Please review code !");
			}
		}
	}

	private int __testFinalCount;
	private long __startTime;
}
