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

import java.util.*;

/**
 * String utility class.
 */
public class DkStrings {
	public static boolean isWhite(String text) {
		return text == null || text.trim().length() == 0;
	}

	public static boolean isEmpty(CharSequence text) {
		return text == null || text.length() == 0;
	}

	public static void requireNotEmpty(String text, String message) {
		if (isEmpty(text)) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * @return 0 (if equals), -1 (if a < b), 1 (if a > b) in lexicography order.
	 */
	public static int compare(CharSequence a, CharSequence b) {
		if (a == null) {
			return b == null ? 0 : -1;
		}
		if (b == null) {
			return 1;
		}

		final int M = a.length();
		final int N = b.length();

		if (M != N) {
			return M < N ? -1 : 1;
		}

		char ch1, ch2;

		for (int i = 0; i < N; ++i) {
			ch1 = a.charAt(i);
			ch2 = b.charAt(i);

			if (ch1 < ch2) {
				return -1;
			}
			if (ch1 > ch2) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Trim with specific target.
	 *
	 * @param text any
	 * @param target target trimed character
	 * @return trimed text
	 */
	public static String trim(String text, char target) {
		final int N = text.length();
		int startIndex = 0;
		int endIndex = N - 1;

		while (startIndex <= endIndex && text.charAt(startIndex) == target) {
			++startIndex;
		}
		while (endIndex >= startIndex && text.charAt(endIndex) == target) {
			--endIndex;
		}

		return (startIndex > 0 || endIndex < N - 1) ? text.substring(startIndex, endIndex + 1) : text;
	}

	/**
	 * Remove start-leading and end-leading characters which inside given targets from text.
	 */
	public static String trim(String text, char... targets) {
		if (text == null || text.length() == 0 || targets == null || targets.length == 0) {
			return text;
		}

		final int N = text.length();
		int startIndex = 0;
		int endIndex = N - 1;

		final Set<Character> set = new HashSet<>();

		for (char ch : targets) {
			set.add(ch);
		}

		while (startIndex <= endIndex && set.contains(text.charAt(startIndex))) {
			++startIndex;
		}

		while (startIndex <= endIndex && set.contains(text.charAt(endIndex))) {
			--endIndex;
		}

		return (startIndex > 0 || endIndex < N - 1) ? text.substring(startIndex, endIndex + 1) : text;
	}

	public static boolean equals(CharSequence a, CharSequence b) {
		return a == b || (a != null && a.equals(b));
	}

	public static String join(char delimiter, String... items) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		for (String item : items) {
			if (first) {
				first = false;
			}
			else {
				sb.append(delimiter);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	public static String join(char delimiter, Iterable<String> items) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = items.iterator();

		if (it.hasNext()) {
			sb.append(it.next());

			while (it.hasNext()) {
				sb.append(delimiter);
				sb.append(it.next());
			}
		}
		return sb.toString();
	}

	public static String join(CharSequence delimiter, Iterable<String> items) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = items.iterator();

		if (it.hasNext()) {
			sb.append(it.next());

			while (it.hasNext()) {
				sb.append(delimiter);
				sb.append(it.next());
			}
		}
		return sb.toString();
	}

	public static String join(CharSequence delimiter, String... items) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		for (String item : items) {
			if (first) {
				first = false;
			}
			else {
				sb.append(delimiter);
			}
			sb.append(item);
		}

		return sb.toString();
	}

	/**
	 * Format with US locale.
	 *
	 * @param format any
	 * @param args any
	 * @return formated text
	 */
	public static String format(String format, Object... args) {
		return format == null || (args == null || args.length == 0) ? format : String.format(Locale.US, format, args);
	}
}
