/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkLogs;
import tool.compet.core4j.DkRunnable;

public class OwnOnFinalObserver<M> extends OwnObserver<M> {
	final DkRunnable action;

	public OwnOnFinalObserver(DkObserver<M> child, DkRunnable action) {
		super(child);
		this.action = action;
	}

	@Override
	public void onFinal() {
		// Run action and pass final-event to child node
		// We will ignore any error since this is last event
		try {
			action.run();
		}
		catch (Exception e) {
			DkLogs.error(this, e);
		}
		finally {
			child.onFinal();
		}
	}
}