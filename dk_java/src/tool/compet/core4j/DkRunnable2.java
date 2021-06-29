/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

/**
 * Extra version of `Runnable` with 2 inputs.
 */
public interface DkRunnable2<A, B> {
	/**
	 * Run (execute) instructions, throws exception when unable to run.
	 *
	 * @throws Exception Unable to run.
	 */
	void run(A a, B b) throws Exception;
}
