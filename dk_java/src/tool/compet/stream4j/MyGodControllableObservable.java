/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

/**
 * God observable node.
 */
class MyGodControllableObservable<M> extends DkObservable<M, MyGodControllableObservable> {
	private final DkControllable<M> controllable;

	MyGodControllableObservable(DkControllable<M> controllable) {
		this.controllable = controllable;
	}

	@Override
	public void subscribeActual(DkObserver<M> child) throws Exception {
		OwnGodControllableObserver<M> wrapper = new OwnGodControllableObserver<>(child, controllable);
		wrapper.start();
	}
}
