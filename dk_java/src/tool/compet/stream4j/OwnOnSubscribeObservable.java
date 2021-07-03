/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkRunnable1;

public class OwnOnSubscribeObservable<M> extends DkObservable<M, OwnOnSubscribeObservable> {
	private final DkRunnable1<DkControllable> action;

	public OwnOnSubscribeObservable(DkObservableSource<M> parent, DkRunnable1<DkControllable> action) {
		this.parent = parent;
		this.action = action;
	}

	@Override
	public void subscribeActual(DkObserver<M> observer) throws Exception {
		parent.subscribe(new OwnOnSubscribeObserver<>(observer, action));
	}
}
