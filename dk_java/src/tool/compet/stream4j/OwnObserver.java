/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkBuildConfig;
import tool.compet.core4j.DkConsoleLogs;

public class OwnObserver<T> implements DkObserver<T> {
	protected final DkObserver<T> child;

	public OwnObserver(DkObserver<T> child) {
		this.child = child;
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		child.onSubscribe(controllable);
	}

	@Override
	public void onNext(T result) throws Exception {
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

		if (DkBuildConfig.DEBUG) {
			if (++__testFinalCount > 1) {
				DkConsoleLogs.warning(this, "Wrong implementation of #onFinal. Please review code !");
			}
		}
	}

	private int __testFinalCount;
}
