/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkCallable1;

/**
 * This observer is flat map which can cancel, pause, resume stream
 *
 * @param <M> model-type of parent node
 * @param <N> model-type of child node
 */
public class OwnFlatMapObserver<M, N> extends OwnBaseMapStreamObserver<M, N> {
	private final DkCallable1<M, DkObservableSource<N>> converter;

	public OwnFlatMapObserver(DkObserver<N> child, DkCallable1<M, DkObservableSource<N>> converter) {
		super(child);
		this.converter = converter;
	}

	@Override
	public void onNext(M result) throws Exception {
		// Use result from parent to create new stream
		// If we got null stream, then consider this as normal map
		// and pass null result to child node
		DkObservableSource<N> nextStream;
		try {
			nextStream = converter.call(result);
		}
		catch (Exception e) {
			nextStream = null;
		}

		if (nextStream == null) {
			child.onNext(null);
		}
		else {
			// We consider this nextStream as a runner, so
			// if nextStream succeed, then we pass result to child node at throwIfFailureObserver,
			// if nextStream failed, then upper node will take care and handle it for us.
			OwnThrowIfFailureObserver<N> throwIfFailureObserver = new OwnThrowIfFailureObserver<>(child);
			nextStream.subscribe(throwIfFailureObserver);
		}
	}
}