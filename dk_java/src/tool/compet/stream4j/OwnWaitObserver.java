/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import java.util.concurrent.atomic.AtomicInteger;

public class OwnWaitObserver<T> extends OwnObserver<T> {
	AtomicInteger onCompleteCount = new AtomicInteger(0);
	AtomicInteger onErrorCount = new AtomicInteger(0);
	AtomicInteger onFinalCount = new AtomicInteger(0);

	public OwnWaitObserver(DkObserver<T> child) {
		super(child);
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		child.onSubscribe(controllable);
	}

	@Override
	public void onNext(T result) throws Exception {
		child.onNext(result);
	}

	@Override
	public void onComplete() throws Exception {
		if (onCompleteCount.incrementAndGet() == 2) {
			notifyComplete();
		}
	}

	@Override
	public void onError(Throwable e) {
		if (onErrorCount.incrementAndGet() == 2) {
			notifyError(e);
		}
	}

	@Override
	public void onFinal() {
		if (onFinalCount.incrementAndGet() == 2) {
			notifyFinal();
		}
	}

	private void notifyComplete() throws Exception {
		child.onComplete();
	}

	private void notifyError(Throwable e) {
		child.onError(e);
	}

	private void notifyFinal() {
		child.onFinal();
	}
}