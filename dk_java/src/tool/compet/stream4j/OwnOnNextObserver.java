/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkRunnable1;

// Own wrapped observer which holds child observer
public class OwnOnNextObserver<M> extends OwnObserver<M> {
	final DkRunnable1<M> action;

	public OwnOnNextObserver(DkObserver<M> child, DkRunnable1<M> action) {
		super(child);
		this.action = action;
	}

	@Override
	public void onNext(M result) throws Exception {
		// Execute action and Pass result to child node
		// Note that, God node will handle error if raised
		action.run(result);
		child.onNext(result);
	}
}