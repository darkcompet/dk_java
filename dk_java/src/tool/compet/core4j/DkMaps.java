/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core4j;

import androidx.annotation.Nullable;

import java.util.Map;

/**
 * This class, provides common basic operations on a collection.
 */
public class DkMaps {
	public static boolean empty(@Nullable Map<?, ?> map) {
		return map == null || map.size() == 0;
	}
}
