/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkRunnable;

public class OwnOnCompleteObserver<M> extends OwnObserver<M> {
	final DkRunnable action;

	public OwnOnCompleteObserver(DkObserver<M> child, DkRunnable action) {
		super(child);
		this.action = action;
	}

	@Override
	public void onComplete() throws Exception {
		// Run action and pass complete-event to child node
		action.run();
		child.onComplete();
	}
}