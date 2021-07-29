/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkCallable1;

/**
 * @param <M> model-type of upper node.
 * @param <N> model-type of lower node.
 */
public class OwnMapObservable<M, N> extends TheObservableSourceImpl<N> {
	private final DkObservableSource<M> parent;
	private final DkCallable1<M, N> converter;

	public OwnMapObservable(DkObservableSource<M> parent, DkCallable1<M, N> converter) {
		this.parent = parent;
		this.converter = converter;
	}

	@Override
	public void subscribeActual(DkObserver<N> observer) throws Exception {
		parent.subscribe(new OwnMapObserver<>(observer, converter));
	}
}
