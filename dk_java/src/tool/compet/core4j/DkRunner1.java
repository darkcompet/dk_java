/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

/**
 * Pass a param to caller without care of exception.
 * Note: it is like with `DkRunnable1`, but take care when run since no exception is declared.
 */
public interface DkRunner1<T> {
	/**
	 * Pass (callback) param to caller.
	 *
	 * @param input Param to pass.
	 */
	void run(T input);
}
