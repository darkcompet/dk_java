/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkCallable;

public class OwnGodCallableObserver<M> extends DkControllable<M> {
	public final DkCallable<M> callable;

	public OwnGodCallableObserver(DkObserver<M> child, DkCallable<M> callable) {
		super(child);
		this.callable = callable;
	}

	public void start() {
		try {
			onSubscribe(this);

			if (isCancel) {
				isCanceled = true;
				return;
			}

			onNext(callable.call());
			onComplete();
		}
		catch (Exception e) {
			onError(e);
		}
		finally {
			onFinal();
		}
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		child.onSubscribe(controllable);
	}
}