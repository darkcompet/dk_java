/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkRunnable;

public class OwnOnCompleteObservable<M> extends TheObservableSourceImpl<M> {
	private final DkRunnable action;

	public OwnOnCompleteObservable(DkObservableSource<M> parent, DkRunnable action) {
		this.parent = parent;
		this.action = action;
	}

	@Override
	public void subscribeActual(DkObserver<M> child) throws Exception {
		parent.subscribe(new OwnOnCompleteObserver<>(child, action));
	}
}
