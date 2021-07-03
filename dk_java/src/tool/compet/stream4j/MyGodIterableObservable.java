/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

/**
 * God observable node.
 */
class MyGodIterableObservable<M> extends DkObservable<M, MyGodIterableObservable> {
	private final Iterable<M> items;

	MyGodIterableObservable(Iterable<M> items) {
		this.items = items;
	}

	@Override
	public void subscribeActual(DkObserver<M> child) throws Exception {
		OwnGodIterableObserver<M> wrapper = new OwnGodIterableObserver<>(child);
		wrapper.start(items);
	}
}
