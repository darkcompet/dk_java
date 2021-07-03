/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkRunnable1;

public class OwnOnErrorObservable<M> extends DkObservable<M, OwnOnErrorObservable> {
	private final DkRunnable1<Throwable> action;

	public OwnOnErrorObservable(DkObservableSource<M> parent, DkRunnable1<Throwable> action) {
		super(parent);
		this.action = action;
	}

	@Override
	public void subscribeActual(DkObserver<M> observer) throws Exception {
		parent.subscribe(new OwnOnErrorObserver<>(observer, action));
	}
}
