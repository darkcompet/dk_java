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
public class OwnMapStreamObserver<M, N> extends OwnBaseMapStreamObserver<M, N> {
	final DkCallable1<M, DkObservableSource<N>> converter;
	OwnWaitObserver<N> myObserver;

	public OwnMapStreamObserver(DkObserver<N> child, DkCallable1<M, DkObservableSource<N>> converter) {
		super(child);
		this.converter = converter;
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		super.onSubscribe(controllable);
	}

	@Override
	public void onNext(M result) throws Exception {
		// Pass result to create new observable
		DkObservableSource<N> myObservable;
		try {
			myObservable = converter.call(result);
		}
		catch (Exception e) {
			myObservable = null;
		}

		// If we got null flat observable, just consider this stream-map is normal map
		if (myObservable == null) {
			child.onNext(null);
			return;
		}

		// Consider given observable is next stream,
		// Run it at same thread with upper node
		myObserver = new OwnWaitObserver<>(child);
		myObservable.subscribe(myObserver);
	}

	@Override
	public void onComplete() throws Exception {
		if (myObserver != null) {
			myObserver.onComplete();
		}
	}

	@Override
	public void onError(Throwable e) {
		if (myObserver != null) {
			myObserver.onError(e);
		}
	}

	@Override
	public void onFinal() {
		if (myObserver != null) {
			myObserver.onFinal();
		}
	}
}