/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

/**
 * This node ONLY take care about 2 events: onSubscribe(), onNext() and onError().
 * That is, other events like onComplete(), onFinal() will be ignored,
 * and not be sent down to child node.
 *
 * - At onSubscribe() event, this just send down controllable to child node.
 * - At onNext() event, this just send down model to child node.
 * - At onError() event, this will throw RuntimeException as follow up specification.
 *
 * @param <T> Model type.
 */
public class OwnThrowIfFailureObserver<T> extends OwnObserver<T> {
	public OwnThrowIfFailureObserver(DkObserver<T> child) {
		super(child);
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		// Just send down to child subcription event
		child.onSubscribe(controllable);
	}

	@Override
	public void onNext(T result) throws Exception {
		// Just tell child success result
		child.onNext(result);
	}

	@Override
	public void onComplete() throws Exception {
		// This node ignores onComplete event
	}

	@Override
	public void onError(Throwable e) {
		// Throws exception as specification so upper stream can take care it.
		throw new RuntimeException(e);
	}

	@Override
	public void onFinal() {
		// This node ignores onFinal event
	}
}