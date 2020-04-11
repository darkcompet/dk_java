/*
 * Copyright (c) 2018 DarkCompet. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tool.compet.javacore.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Utility class, provides common basic operations on object.
 */
public class DkObjects {
	/**
	 * @param obj any
	 * @param <T> type of obj
	 * @return NullPointerException
	 */
	public static <T> T requireNonNull(T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
	}

	/**
	 * @param obj any
	 * @param <T> type of obj
	 * @return NullPointerException
	 */
	public static <T> T requireNonNull(T obj, String format, Object... args) {
		if (obj == null) {
			throw new NullPointerException(DkStrings.format(format, args));
		}
		return obj;
	}

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection) obj).size() == 0;
		}
		if (obj instanceof Map) {
			return ((Map) obj).size() == 0;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		return false;
	}

	public static boolean equals(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}
}
