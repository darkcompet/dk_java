/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

/**
 * This is convenience build-config for the associated app.
 */
public final class BuildConfig {
	// Because inside Java-based libraries, some one uses this debug field
	// to perform some extras actions as logging, testing...
	// So to make them run at debug env, the app should enable
	// this debug field at debug version (should NOT at product version)
	public static boolean DEBUG;
}