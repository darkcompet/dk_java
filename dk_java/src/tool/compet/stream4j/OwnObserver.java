/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.BuildConfig;
import tool.compet.core4j.DkLogs;

/**
 * @param <M> Model type.
 */
public class OwnObserver<M> implements DkObserver<M> {
	protected final DkObserver<M> child;

	public OwnObserver(DkObserver<M> child) {
		this.child = child;
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		child.onSubscribe(controllable);
	}

	@Override
	public void onNext(M result) throws Exception {
		child.onNext(result);
	}

	@Override
	public void onError(Throwable e) {
		child.onError(e);
	}

	@Override
	public void onComplete() throws Exception {
		child.onComplete();
	}

	@Override
	public void onFinal() {
		child.onFinal();

		if (BuildConfig.DEBUG) {
			if (++__testFinalCount > 1) {
				DkLogs.warning(this, "Wrong implementation of #onFinal. Please review code !");
			}
		}
	}

	private int __testFinalCount;
}
