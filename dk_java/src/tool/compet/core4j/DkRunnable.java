/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

/**
 * Same version of `Runnable` without input.
 */
public interface DkRunnable {
	/**
	 * Run (execute) instructions, throws exception when unable to run.
	 *
	 * @throws Exception Unable to run.
	 */
	void run() throws Exception;
}
