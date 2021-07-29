/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkCallable;

public class OwnGodCallableObserver<M> extends DkControllable<M> {
	protected final DkCallable<M> callable;

	public OwnGodCallableObserver(DkObserver<M> child, DkCallable<M> callable) {
		super(child);
		this.callable = callable;
	}

	public void start() {
		try {
			child.onSubscribe(this);

			if (isCancel) {
				isCanceled = true;
				return;
			}

			child.onNext(callable.call());
			child.onComplete();
		}
		catch (Exception e) {
			child.onError(e);
		}
		finally {
			child.onFinal();
		}
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		child.onSubscribe(controllable);
	}
}