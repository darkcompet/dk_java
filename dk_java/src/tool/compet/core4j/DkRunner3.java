/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

/**
 * Pass 3 params to caller without care of exception.
 * Note: it is like with `DkRunnable3`, but take care when run since no exception is declared.
 */
public interface DkRunner3<A, B, C> {
	/**
	 * Pass (callback) param to caller.
	 *
	 * @param a Param 1 to pass.
	 * @param b Param 2 to pass.
	 * @param c Param 3 to pass.
	 */
	void run(A a, B b, C c);
}
