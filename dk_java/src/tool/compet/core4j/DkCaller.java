/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

/**
 * It computes output without input.
 * Note: it is like with `DkCallable`, but take care when call since no exception is declared.
 */
public interface DkCaller<R> {
	/**
	 * Computes result from an input without exception thrown.
	 *
	 * @return Result without exception declared.
	 */
	R call();
}
