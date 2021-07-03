/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkRunnable1;

public class OwnOnErrorObserver<M> extends OwnObserver<M> {
	final DkRunnable1<Throwable> action;

	public OwnOnErrorObserver(DkObserver<M> child, DkRunnable1<Throwable> action) {
		super(child);
		this.action = action;
	}

	@Override
	public void onError(Throwable throwable) {
		try {
			action.run(throwable);
			child.onError(throwable);
		}
		catch (Exception e) {
			child.onError(e);
		}
	}
}