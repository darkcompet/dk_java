/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.database4j;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tool.compet.core4j.DkTypeHelper;
import tool.compet.core4j.DkCollections;

public class OwnGrammarHelper {
	public static String wrapName(String name) {
		if ("*".equals(name)) {
			return "*";
		}
		name = name.trim();
		if (name.toLowerCase().contains(" as ")) {
			String[] arr = name.split("\\s+(?i)as\\s+"); // (?i) for case-insensitive
			return wrapName(arr[0]) + " as " + wrapName(arr[1]);
		}
		if (name.contains(".")) {
			String[] arr = name.split("\\.");
			return wrapName(arr[0]) + '.' + wrapName(arr[1]);
		}
		return '`' + name + '`';
	}

	public static List<String> wrapNameList(Collection<String> names) {
		List<String> items = new ArrayList<>();
		if (!DkCollections.empty(names)) {
			for (String name : names) {
				items.add('`' + name + '`');
			}
		}
		return items;
	}

	public static String wrapPrimitiveValue(@Nullable Object value) {
		if (value == null) {
			return null;
		}

		final String singleQuote = "'";
		final String doubleQuote = "''";

		if (value instanceof String) {
			return singleQuote + ((String) value).replace(singleQuote, doubleQuote) + singleQuote;
		}
		return singleQuote + value.toString().replace(singleQuote, doubleQuote) + singleQuote;
	}

	public static List<String> wrapPrimitiveValues(Iterable<?> values) {
		List<String> items = new ArrayList<>();
		for (Object value : values) {
			items.add(wrapPrimitiveValue(value));
		}
		return items;
	}

	public static Object toDbValue(Object obj) {
		if (obj == null) {
			return null;
		}
		if (DkTypeHelper.typeMasked(obj.getClass()) == DkTypeHelper.TYPE_BOOLEAN_MASKED) {
			return ((boolean) obj) ? 1 : 0;
		}
		return obj;
	}
}
