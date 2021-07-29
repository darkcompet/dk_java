/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import tool.compet.core4j.DkStrings;
import tool.compet.core4j.DkUtils;

public class OwnServiceMethod {
	/**
	 * Static fields (NOT be re-assigned anymore).
	 */

	// Request method: GET, POST...
	private String originRequestMethod;
	// Header key-value pairs
	private final SimpleArrayMap<String, String> originHeaders;
	// This contains `alias` (for eg,. user_id, app_name) for replacement
	private String originRelativeUrl;

	/**
	 * Dynamic fields (be re-assigned when re-build).
	 */

	// Header key-value pairs
	private final SimpleArrayMap<String, String> __headers;
	// Full request url
	private String __link;
	// Body for post request
	private byte[] __body;
	// Relative url which contains query string if provided
	private String __queriableRelativeUrl;

	OwnServiceMethod(Method method) {
		this.originHeaders = new ArrayMap<>();
		this.__headers = new ArrayMap<>();

		originParseOnMethod(method);
	}

	// region Parsed result

	protected String requestMethod() {
		return originRequestMethod;
	}

	protected byte[] body() {
		return __body;
	}

	protected SimpleArrayMap<String, String> headers() {
		__headers.putAll(originHeaders);
		return __headers;
	}

	protected String link() {
		return __link;
	}

	// endregion Parsed result

	// region Origin parsing

	/**
	 * Origin parse info from given method.
	 * Note that, it is parsed ONLY one time when this service method is initialized.
	 */
	private void originParseOnMethod(Method method) {
		Annotation[] methodAnnotations = method.getDeclaredAnnotations();

		if (methodAnnotations.length == 0) {
			DkUtils.complainAt(this, "Must annotate each method with One of @DkGet, @DkPost...");
		}

		for (Annotation annotation : methodAnnotations) {
			if (annotation instanceof DkHttpHeaderEntry) {
				originParseHeaderOnMethod((DkHttpHeaderEntry) annotation);
			}
			else if (annotation instanceof DkHttpGetRequest) {
				originParseGetRequestOnMethod((DkHttpGetRequest) annotation);
			}
			else if (annotation instanceof DkHttpPostRequest) {
				originParsePostRequestOnMethod((DkHttpPostRequest) annotation);
			}
		}

		if (originRequestMethod == null) {
			DkUtils.complainAt(this, "Missing request method annotation on the method: " + method);
		}
	}

	/**
	 * Origin parse header info.
	 */
	private void originParseHeaderOnMethod(DkHttpHeaderEntry headerInfo) {
		originHeaders.put(headerInfo.key(), headerInfo.value());
	}

	/**
	 * Origin parse info with GET request method.
	 */
	private void originParseGetRequestOnMethod(DkHttpGetRequest getRequest) {
		if (originRequestMethod != null) {
			DkUtils.complainAt(this, "Can specify only one request method");
		}

		originRequestMethod = DkHttpConst.GET;
		originRelativeUrl = DkStrings.trimMore(getRequest.value(), '/');

		switch (getRequest.contentType()) {
			case DkHttpConst.APPLICATION_JSON: {
				originHeaders.put(DkHttpConst.CONTENT_TYPE, DkHttpConst.APPLICATION_JSON);
				break;
			}
			case DkHttpConst.X_WWW_FORM_URLENCODED: {
				originHeaders.put(DkHttpConst.CONTENT_TYPE, DkHttpConst.X_WWW_FORM_URLENCODED);
				break;
			}
		}
	}

	/**
	 * Parse info with POST request method.
	 */
	private void originParsePostRequestOnMethod(DkHttpPostRequest postRequest) {
		if (originRequestMethod != null) {
			DkUtils.complainAt(this, "Can specify only one request method");
		}

		originRequestMethod = DkHttpConst.POST;
		originRelativeUrl = DkStrings.trimMore(postRequest.value(), '/');

		switch (postRequest.contentType()) {
			case DkHttpConst.APPLICATION_JSON: {
				originHeaders.put(DkHttpConst.CONTENT_TYPE, DkHttpConst.APPLICATION_JSON);
				break;
			}
			case DkHttpConst.X_WWW_FORM_URLENCODED: {
				originHeaders.put(DkHttpConst.CONTENT_TYPE, DkHttpConst.X_WWW_FORM_URLENCODED);
				break;
			}
		}
	}

	// endregion Origin parsing

	/**
	 * This is called every time when the method is invoked
	 * since this must rebuild with new data of parameters.
	 * We should consider it as dynamic build method.
	 */
	protected void build(String baseUrl, Method method, Object[] methodParams) {
		// Reset dynamic fields before re-build
		__headers.clear();
		__link = baseUrl;
		__body = null;
		__queriableRelativeUrl = originRelativeUrl;

//		tmp_postFormData = null;
//		tmp_relativeUrl = null;

		// Parse method parameter annotations
		dynamicParseOnParams(method, methodParams);

		__link += __queriableRelativeUrl;

//		if (tmp_postFormData != null) {
//			try {
//				dynamic_body = URLEncoder.encode(tmp_postFormData.toString(), "UTF-8").getBytes(Charset.forName("UTF-8"));
//			}
//			catch (Exception e) {
//				DkLogs.error(this, e);
//			}
//		}
	}

	// region Dynamic parsing (re-build)

	// Dynamic parsing on rebuild
	private void dynamicParseOnParams(Method method, Object[] methodParams) {
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		StringBuilder query = new StringBuilder();

		for (int i = paramAnnotations.length - 1; i >= 0; --i) {
			for (Annotation annotation : paramAnnotations[i]) {
				if (annotation instanceof DkHttpUrlReplacement) {
					parseUrlReplacementOnParams((DkHttpUrlReplacement) annotation, methodParams[i]);
				}
				else if (annotation instanceof DkHttpHeaderEntry) {
					parseHeaderEntryOnParams((DkHttpHeaderEntry) annotation, methodParams[i]);
				}
				else if (annotation instanceof DkHttpQuery) {
					parseQueryOnParams(query, (DkHttpQuery) annotation, methodParams[i]);
				}
				else if (annotation instanceof DkHttpBody) {
					parseBodyOnParams((DkHttpBody) annotation, methodParams[i]);
				}
//				else if (annotation instanceof DkHttpBodyFormDataEntry) {
//					dynamicParseOnParams((DkHttpBodyFormDataEntry) annotation, methodParams[i]);
//				}
			}
		}

		// Build full relative url (relative path + query string)
		if (query.length() > 0) {
			__queriableRelativeUrl += "?" + query;
		}
	}

	// Dynamic parsing on rebuild
	private void parseUrlReplacementOnParams(DkHttpUrlReplacement urlReplacement, Object paramValue) {
		// url: app/{name}
		// alias: name
		// replacement: gpscompass
		// -> final url: app/gpscompass
		String alias = urlReplacement.value();
		String replacement = paramValue instanceof String ? (String) paramValue : String.valueOf(paramValue);

		// Maybe alias placed in static relative url,
		// so we need replace them with value of method's parameter
		while (true) {
			String target = "{" + alias + "}";
			int index = __queriableRelativeUrl.indexOf(target);
			if (index < 0) {
				break;
			}
			__queriableRelativeUrl = __queriableRelativeUrl.replace(target, replacement);
		}
	}

	// Dynamic parsing on rebuild
	private void parseHeaderEntryOnParams(DkHttpHeaderEntry headerEntry, Object paramValue) {
		String key = headerEntry.key();
		String value = String.valueOf(paramValue);

		if (! DkStrings.empty(headerEntry.value())) {
			DkUtils.complainAt(this, "Pls don't use `value()` in `DkHttpHeaderEntry` for params");
		}

		__headers.put(key, value);
	}

	// Dynamic parsing on rebuild
	private void parseQueryOnParams(StringBuilder query, DkHttpQuery queryInfo, Object paramValue) {
		if (query.length() > 0) {
			query.append('&');
		}
		query.append(queryInfo.value()).append("=").append(paramValue);
	}

	// Dynamic parsing on rebuild
	private void parseBodyOnParams(DkHttpBody bodyInfo, Object paramValue) {
		if (! (paramValue instanceof byte[])) {
			throw new RuntimeException("Body must be in `byte[]`");
		}
		__body = (byte[]) paramValue;
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

	// endregion Dynamic parsing (re-build)
}
