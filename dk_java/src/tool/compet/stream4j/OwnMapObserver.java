/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkCallable1;

/**
 * @param <I> Input model type.
 * @param <O> Output model type.
 */
public class OwnMapObserver<I, O> implements DkObserver<I> {
	public final DkObserver<O> child;
	public final DkCallable1<I, O> converter;

	public OwnMapObserver(DkObserver<O> child, DkCallable1<I, O> converter) {
		this.child = child;
		this.converter = converter;
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		child.onSubscribe(controllable);
	}

	@Override
	public void onNext(I result) throws Exception {
		child.onNext(converter.call(result));
	}

	@Override
	public void onError(Throwable e) {
		child.onError(e);
	}

	@Override
	public void onComplete() throws Exception {
		child.onComplete();
	}

	@Override
	public void onFinal() {
		child.onFinal();
	}
}