/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import tool.compet.core4j.DkConsoleLogs;
import tool.compet.core4j.DkRunnable;

class MyOnFinalObservable<T> extends DkObservable<T> {
	private final DkRunnable action;

	MyOnFinalObservable(DkObservable<T> parent, DkRunnable action) {
		super(parent);
		this.action = action;
	}

	@Override
	protected void subscribeActual(DkObserver<T> child) {
		parent.subscribe(new OnFinalObserver<>(child, action));
	}

	static class OnFinalObserver<R> extends OwnObserver<R> {
		final DkRunnable action;

		OnFinalObserver(DkObserver<R> child, DkRunnable action) {
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
				DkConsoleLogs.error(this, e);
			}
			finally {
				child.onFinal();
			}
		}
	}
}
