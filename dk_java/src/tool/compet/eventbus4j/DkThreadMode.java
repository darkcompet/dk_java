/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.eventbus4j;

/**
 * Run mode for Subscriber method.
 */
public interface DkThreadMode {
	// Subscriber method will run on same thread with Poster
	int POSTER = 1;

	// Subscriber method will run on background thread without ordering
	int ASYNC = 2;

	// Subscriber method will run on background thread in ordered
	int ASYNC_ORDERED = 3;

	// Defaul mode, subscriber method will run on Android main thread
	int ANDROID_MAIN = 4;

	// Subscriber method will run on Android main thread in ordered
	int ANDROID_MAIN_ORDERED = 5;
}
