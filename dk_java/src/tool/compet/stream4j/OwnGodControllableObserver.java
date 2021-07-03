/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

public class OwnGodControllableObserver<M> extends DkControllable<M> implements DkObserver<M> {
	public final DkControllable<M> controllable;

	public OwnGodControllableObserver(DkObserver<M> child, DkControllable<M> controllable) {
		super(child);
		this.controllable = controllable;
	}

	public void start() {
		try {
			onSubscribe(this);

			if (isCancel) {
				isCanceled = true;
				return;
			}

			onNext(controllable.call());
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