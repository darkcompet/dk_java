/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import androidx.collection.ArrayMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

import tool.compet.core4j.DkConsoleLogs;
import tool.compet.core4j.DkStrings;
import tool.compet.core4j.DkUtils;

public class OwnServiceMethod {
	// Header key-value pairs
	final Map<String, String> headers;

	// Request method: GET, POST...
	String requestMethod;

	// Completely url
	String url;

	// Post body
	byte[] body;

	// Building process temporary data
	private String tmpBaseUrl;
	private String tmpRelativeUrl;
	private StringBuilder tmpFormData;

	OwnServiceMethod(String baseUrl, Method method) {
		this.tmpBaseUrl = baseUrl;
		this.headers = new ArrayMap<>();
//		this.responseClass = DkReflections.getLastGenericReturnClass(method);

		parseOnMethod(method);
	}

	/**
	 * Parse info from given method. Note that, it is parsed only one time.
	 */
	private void parseOnMethod(Method method) {
		Annotation[] methodAnnotations = method.getDeclaredAnnotations();

		if (methodAnnotations.length == 0) {
			DkUtils.complainAt(this, "Must annotate each method with One of @DkGet, @DkPost...");
		}

		for (Annotation annotation : methodAnnotations) {
			if (annotation instanceof DkHeader) {
				parseOnMethod((DkHeader) annotation);
			}
			else if (annotation instanceof DkGet) {
				parseOnMethod((DkGet) annotation);
			}
			else if (annotation instanceof DkPost) {
				parseOnMethod((DkPost) annotation);
			}
		}

		if (requestMethod == null) {
			DkUtils.complainAt(this, "Missing request method annotation on the method: " + method);
		}
		if (DkStrings.white(tmpRelativeUrl)) {
			DkUtils.complainAt(this, "Invalid relative url: " + tmpRelativeUrl);
		}

		tmpRelativeUrl = DkStrings.trimMore(tmpRelativeUrl, '/');
	}

	private void parseOnMethod(DkHeader headerInfo) {
		headers.put(headerInfo.key(), headerInfo.value());
	}

	private void parseOnMethod(DkGet getInfo) {
		if (requestMethod != null) {
			DkUtils.complainAt(this, "Can specify only one request method");
		}

		requestMethod = DkHttpConst.GET;
		tmpRelativeUrl = getInfo.value();

		switch (getInfo.responseFormat()) {
			case DkHttpConst.APPLICATION_JSON: {
				headers.put(DkHttpConst.CONTENT_TYPE, DkHttpConst.APPLICATION_JSON);
				break;
			}
			case DkHttpConst.X_WWW_FORM_URLENCODED: {
				headers.put(DkHttpConst.CONTENT_TYPE, DkHttpConst.X_WWW_FORM_URLENCODED);
				break;
			}
		}
	}

	private void parseOnMethod(DkPost postInfo) {
		if (requestMethod != null) {
			DkUtils.complainAt(this, "Can specify only one request method");
		}

		requestMethod = DkHttpConst.POST;
		tmpRelativeUrl = postInfo.value();

		switch (postInfo.responseFormat()) {
			case DkHttpConst.APPLICATION_JSON: {
				headers.put(DkHttpConst.CONTENT_TYPE, DkHttpConst.APPLICATION_JSON);
				break;
			}
			case DkHttpConst.X_WWW_FORM_URLENCODED: {
				headers.put(DkHttpConst.CONTENT_TYPE, DkHttpConst.X_WWW_FORM_URLENCODED);
				break;
			}
		}
	}

	void build(Method method, Object[] methodParams) {
		// Reset dynamic fields before building
		url = null;
		body = null;

		// Parse method parameter annotations
		parseOnParams(method, methodParams);

		url = tmpBaseUrl + tmpRelativeUrl;

		if (tmpFormData != null) {
			try {
				body = URLEncoder.encode(tmpFormData.toString(), "UTF-8").getBytes(Charset.forName("UTF-8"));
			}
			catch (Exception e) {
				DkConsoleLogs.error(this, e);
			}
		}
	}

	private void parseOnParams(Method method, Object[] methodParams) {
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		StringBuilder query = new StringBuilder();

		for (int i = paramAnnotations.length - 1; i >= 0; --i) {
			for (Annotation annotation : paramAnnotations[i]) {
				if (annotation instanceof DkUrlReplacement) {
					parseOnParams((DkUrlReplacement) annotation, methodParams[i]);
				}
				else if (annotation instanceof DkHeader) {
					parseOnParams((DkHeader) annotation, methodParams[i]);
				}
				else if (annotation instanceof DkQuery) {
					parseOnParams(query, (DkQuery) annotation, methodParams[i]);
				}
				else if (annotation instanceof DkBody) {
					parseOnParams((DkBody) annotation, methodParams[i]);
				}
				else if (annotation instanceof DkUrlEncoded) {
					parseOnParams((DkUrlEncoded) annotation, methodParams[i]);
				}
			}
		}

		if (query.length() > 0) {
			tmpRelativeUrl += "?" + query;
		}
	}

	private void parseOnParams(DkUrlReplacement replaceUrlInfo, Object paramValue) {
		String nodeName = replaceUrlInfo.value();
		String value = paramValue instanceof String ? (String) paramValue : String.valueOf(paramValue);

		while (true) {
			int index = tmpRelativeUrl.indexOf(nodeName);

			if (index < 0) {
				break;
			}

			tmpRelativeUrl = tmpRelativeUrl.replace(nodeName, value);
		}
	}

	private void parseOnParams(DkHeader headerInfo, Object paramValue) {
		String key = headerInfo.key();
		String value = String.valueOf(paramValue);

		if (!DkStrings.empty(headerInfo.value())) {
			DkUtils.complainAt(this, "Don't use #value() in #DkHeader for params");
		}

		headers.put(key, value);
	}

	private void parseOnParams(StringBuilder query, DkQuery queryInfo, Object paramValue) {
		if (query.length() > 0) {
			query.append('&');
		}
		query.append(queryInfo.value()).append("=").append(paramValue);
	}

	private void parseOnParams(DkBody bodyInfo, Object paramValue) {
		if (! (paramValue instanceof byte[])) {
			throw new RuntimeException("Body type must be `byte[]`");
		}
		body = (byte[]) paramValue;
//		body = ((String) paramValue).getBytes();
//		else if (paramValue.getClass().isPrimitive()) {
//			body = String.valueOf(paramValue).getBytes();
//		}
//		else if (paramValue instanceof Bitmap) {
//			body = DkBitmaps.toByteArray((Bitmap) paramValue);
//		}
//		else {
//			body = DkJsonConverter.getIns().obj2json(paramValue).getBytes();
//		}
	}

	private void parseOnParams(DkUrlEncoded bodyInfo, Object paramValue) {
		if (tmpFormData == null) {
			tmpFormData = new StringBuilder(256);
		}
		if (tmpFormData.length() > 0) {
			tmpFormData.append('&');
		}
		tmpFormData.append(bodyInfo.value()).append("=").append(paramValue);
	}
}
