/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

/**
 * Imagine that some where in stream has occured exception at some node,
 * but we wanna ignore that error and continue to process it,
 * <p>
 * To resolve this, we introduce this node to switch to `onNext()` at `onError()` event.
 * So child node
 */
public class OwnIgnoreErrorObservable<M> extends TheObservableSourceImpl<M> {
	public OwnIgnoreErrorObservable(DkObservableSource<M> parent) {
		this.parent = parent;
	}

	@Override
	public void subscribeActual(DkObserver<M> observer) throws Exception {
		parent.subscribe(new OwnIgnoreErrorObserver<>(observer));
	}
}
