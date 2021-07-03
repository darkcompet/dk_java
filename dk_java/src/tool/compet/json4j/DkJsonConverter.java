/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.json4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tool.compet.core4j.DkDateTimeConst;
import tool.compet.core4j.DkConsoleLogs;

/**
 * Dependency: com.google.code.gson:gson:2.8.6
 */
public class DkJsonConverter {
	private static DkJsonConverter INS;
	private final Gson gson;

	private DkJsonConverter() {
		gson = new GsonBuilder()
			.setDateFormat(DkDateTimeConst.DATETIME)
			.excludeFieldsWithoutExposeAnnotation()
			.setPrettyPrinting()
			.create();
	}

	public static DkJsonConverter getIns() {
		return INS != null ? INS : (INS = new DkJsonConverter());
	}

	/**
	 * Converts JSON to POJO of given type.
	 *
	 * @return POJO object if succeed. Otherwise (failed) returns null.
	 */
	public <T> T json2obj(String json, Class<T> classOfT) {
		try {
			return gson.fromJson(json, classOfT);
		}
		catch (Exception e) {
			DkConsoleLogs.error(this, e);
		}
		return null;
	}

	/**
	 * Converts POJO to JSON.
	 */
	public String obj2json(Object obj) {
		return gson.toJson(obj);
	}
}
