/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkRunnable1;

public class OwnOnSubscribeObserver<M> extends OwnObserver<M> {
	final DkRunnable1<DkControllable> action;

	public OwnOnSubscribeObserver(DkObserver<M> child, DkRunnable1<DkControllable> action) {
		super(child);
		this.action = action;
	}

	@Override
	public void onSubscribe(DkControllable controllable) throws Exception {
		// Run incoming task and pass controllable to child node
		action.run(controllable);
		child.onSubscribe(controllable);
	}
}