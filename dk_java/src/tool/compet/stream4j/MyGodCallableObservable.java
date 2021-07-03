/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkCallable;

/**
 * God observable node.
 * When start, this run callable and then handle next events.
 */
class MyGodCallableObservable<M> extends DkObservable<M, MyGodCallableObservable> {
	private final DkCallable<M> execution;

	MyGodCallableObservable(DkCallable<M> execution) {
		this.execution = execution;
	}

	@Override
	public void subscribeActual(DkObserver<M> child) throws Exception {
		OwnGodCallableObserver<M> wrapper = new OwnGodCallableObserver<>(child, execution);
		wrapper.start();
	}
}
