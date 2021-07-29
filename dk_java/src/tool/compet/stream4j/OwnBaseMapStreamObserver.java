/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import java.util.concurrent.Callable;

/**
 * Extends this class to switch from upper-stream to lower-stream.
 *
 * @param <M> model-type of upper stream
 * @param <N> model-type of lower stream
 */
public abstract class OwnBaseMapStreamObserver<M, N>
	extends OwnControllable
	implements Callable<N>, DkObserver<M> {

	protected DkObserver<N> child;

	protected OwnBaseMapStreamObserver(DkObserver<N> child) {
		this.child = child;
	}

	@Override
	public N call() throws Exception {
		throw new RuntimeException("Must implement this method");
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		this.parent = controllable;
		child.onSubscribe(controllable);
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
	}
}
