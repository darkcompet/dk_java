/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import tool.compet.core4j.BuildConfig;
import tool.compet.core4j.DkConsoleLogs;
import tool.compet.core4j.DkUtils;

/**
 * It is convenience class. It will create new instance of http api requester,
 * called as ServiceApi, so caller can use it to request to server.
 * Usage example:
 * <pre>
 *    // Create new instance of UserApi
 *    UserApi userApi = DkApiService.newIns()
 *       .configWith(App.getContext(), "server/server_darkcompet_apps.json")
 *       .create(UserApi.class);
 *
 * 	// Now we can request to server via methods inside userApi
 *    ProfileResponse profileResponse = userApi
 *       .downloadProfile(accessToken)
 *       .map(res -> ResponseValidator.validate(res).response)
 *       .scheduleInBackgroundAndObserveOnMainThread()
 *       .subscribe();
 * </pre>
 */
public class DkApiService {
	protected String baseUrl;
	protected String credential;
	protected int connectTimeoutMillis;
	protected int readTimeoutMillis;

	protected final SimpleArrayMap<Method, OwnServiceMethod<?>> serviceMethods;

	public DkApiService() {
		serviceMethods = new ArrayMap<>();
	}

	public DkApiService setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	/**
	 * Static set basic credential to authenticate with server.
	 * Note that, dynamic adding should be performed in Service's methods.
	 *
	 * @param credential Normally, it is string at base64 of `username:password`;
	 */
	public DkApiService setBasicCredential(String credential) {
		this.credential = DkHttpConst.BASIC + credential;
		return this;
	}

	/**
	 * Static set bearer credential to authenticate with server.
	 * Note that, dynamic adding should be performed in Service's methods.
	 */
	public DkApiService setBearerCredential(String auth) {
		this.credential = DkHttpConst.BEARER + auth;
		return this;
	}

	public DkApiService setConnectTimeoutMillis(int connectTimeoutSecond) {
		this.connectTimeoutMillis = connectTimeoutSecond;
		return this;
	}

	public DkApiService setReadTimeoutMillis(int readTimeoutSecond) {
		this.readTimeoutMillis = readTimeoutSecond;
		return this;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getCredential() {
		return credential;
	}

	public long getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}

	public long getReadTimeoutMillis() {
		return readTimeoutMillis;
	}

	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> serviceClass) {
		validateConfig();

		InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
			// Don't handle method which is not in service class
			if (! method.getDeclaringClass().equals(serviceClass)) {
				return method.invoke(proxy, args);
			}

			return callApi(method, args);
		};

		return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, handler);
	}

	private void validateConfig() {
		if (baseUrl == null) {
			DkUtils.complainAt(this, "Must specify non-null baseUrl");
		}
		if (!baseUrl.endsWith("/")) {
			baseUrl += '/';
		}
	}

	private TheHttpResponse callApi(Method method, Object[] args) throws Exception {
		// Create and cache service method
		OwnServiceMethod<?> serviceMethod;

		synchronized (serviceMethods) {
			serviceMethod = serviceMethods.get(method);
		}

		if (serviceMethod == null) {
			serviceMethod = new OwnServiceMethod<>(baseUrl, method);

			synchronized (serviceMethods) {
				serviceMethods.put(method, serviceMethod);
			}
		}

		final OwnServiceMethod<?> finalServiceMethod = serviceMethod;

		// Rebuild arguments of service method since args are dynamic
		String requestMethod;
		byte[] body;
		ArrayMap<String, String> headers = new ArrayMap<>();
		String url;
		Class<?> responseClass;

		synchronized (finalServiceMethod) {
			finalServiceMethod.build(method, args);

			requestMethod = finalServiceMethod.requestMethod;
			body = finalServiceMethod.body;
			headers.putAll(finalServiceMethod.headers);
			url = finalServiceMethod.url;
			responseClass = finalServiceMethod.responseClass;
		}

		// Start request to server with parsed info
		return startRequest(requestMethod, body, headers, url, responseClass);
	}

	private <R> TheHttpResponse startRequest(String requestMethod, byte[] body,
		SimpleArrayMap<String, String> headers, String url, Class<R> responseClass) throws Exception {

		DkHttpClient httpClient = new DkHttpClient(url)
			.setReadTimeout(readTimeoutMillis)
			.setConnectTimeout(connectTimeoutMillis)
			.setRequestMethod(requestMethod)
			.setBody(body);

		if (credential != null) {
			httpClient.addToHeader(DkHttpConst.AUTHORIZATION, credential);
		}

		httpClient.addAllToHeader(headers);

		if (BuildConfig.DEBUG) {
			DkConsoleLogs.info(this, "Network request at thread: %s", Thread.currentThread().toString());
		}

		return httpClient.execute();
	}
}
