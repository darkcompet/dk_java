/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkCallable1;

/**
 * Like as normal map, this also maps result from parent node to other type at child node.
 * But this is mapping other stream, so it is convenient method for join streams and run
 * them as straight chain.
 *
 * @param <M> model-type of parent node
 * @param <N> model-type of child node
 */
public class OwnMapStreamObservable<M, N> extends TheObservableSourceImpl<N> {
	private final DkObservableSource<M> parent;
	private final DkCallable1<M, DkObservableSource<N>> converter;

	public OwnMapStreamObservable(DkObservableSource<M> parent, DkCallable1<M, DkObservableSource<N>> converter) {
		this.parent = parent;
		this.converter = converter;
	}

	@Override
	public void subscribeActual(DkObserver<N> child) throws Exception {
		parent.subscribe(new OwnFlatMapObserver<>(child, converter));
	}
}
