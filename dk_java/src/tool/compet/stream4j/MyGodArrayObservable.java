/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import java.util.Arrays;
import java.util.Collections;

/**
 * God observable node.
 */
class MyGodArrayObservable<M> extends DkObservable<M, MyGodArrayObservable> {
	private final Iterable<M> items;

	MyGodArrayObservable(M item) {
		this.items = Collections.singletonList(item);
	}

	MyGodArrayObservable(M[] items) {
		this.items = Arrays.asList(items);
	}

	@Override
	public void subscribeActual(DkObserver<M> child) throws Exception {
		OwnGodArrayObserver<M> wrapper = new OwnGodArrayObserver<>(child);
		wrapper.start(items);
	}
}
