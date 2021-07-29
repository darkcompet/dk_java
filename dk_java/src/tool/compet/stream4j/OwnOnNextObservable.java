/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkRunnable1;

/**
 * This node just execute `Runnable` from caller and pass the result from above-node to under-node.
 */
public class OwnOnNextObservable<M> extends TheObservableSourceImpl<M> {
	private final DkObservableSource<M> parent;
	private final DkRunnable1<M> action;

	public OwnOnNextObservable(DkObservableSource<M> parent, DkRunnable1<M> action) {
		this.parent = parent;
		this.action = action;
	}

	@Override
	public void subscribeActual(DkObserver<M> observer) throws Exception {
		// Just send to parent own wrapped observer
		parent.subscribe(new OwnOnNextObserver<>(observer, action));
	}
}
