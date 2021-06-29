/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

/**
 * Pass 2 params to caller without care of exception.
 * Note: it is like with `DkRunnable2`, but take care when run since no exception is declared.
 */
public interface DkRunner2<A, B> {
	/**
	 * Pass (callback) param to caller.
	 *
	 * @param a Param 1 to pass.
	 * @param b Param 2 to pass.
	 */
	void run(A a, B b);
}
