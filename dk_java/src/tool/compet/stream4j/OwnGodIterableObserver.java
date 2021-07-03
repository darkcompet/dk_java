/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

public class OwnGodIterableObserver<M> extends DkControllable<M> implements DkObserver<M> {
	public OwnGodIterableObserver(DkObserver<M> child) {
		super(child);
	}

	public void start(Iterable<M> items) {
		try {
			onSubscribe(this);

			if (isCancel) {
				isCanceled = true;
				return;
			}

			for (M item : items) {
				if (isCancel) {
					isCanceled = true;
					break;
				}
				onNext(item);
			}

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