/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import tool.compet.core4j.DkBuildConfig;
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
@SuppressWarnings("unchecked")
public class DkApiService<T extends DkApiService> {
	protected String baseUrl;
	protected String credential;
	protected int connectTimeoutMillis;
	protected int readTimeoutMillis;

	protected final SimpleArrayMap<Method, OwnServiceMethod> serviceMethods;

	public DkApiService() {
		serviceMethods = new ArrayMap<>();
	}

	public T setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return (T) this;
	}

	/**
	 * Static set basic credential to authenticate with server.
	 * Note that, dynamic adding should be performed in Service's methods.
	 *
	 * @param credential Normally, it is string at base64 of `username:password`;
	 */
	public T setBasicCredential(String credential) {
		this.credential = DkHttpConst.BASIC_AUTH + credential;
		return (T) this;
	}

	/**
	 * Static set bearer credential to authenticate with server.
	 * Note that, dynamic adding should be performed in Service's methods.
	 */
	public T setBearerCredential(String credential) {
		this.credential = DkHttpConst.BEARER_AUTH + credential;
		return (T) this;
	}

	public T setConnectTimeoutMillis(int connectTimeoutSecond) {
		this.connectTimeoutMillis = connectTimeoutSecond;
		return (T) this;
	}

	public T setReadTimeoutMillis(int readTimeoutSecond) {
		this.readTimeoutMillis = readTimeoutSecond;
		return (T) this;
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
	public <S> S create(Class<S> serviceClass) {
		validateConfig();

		InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
			// Don't handle method which is not in service class
			if (! method.getDeclaringClass().equals(serviceClass)) {
				return method.invoke(proxy, args);
			}

			return callApi(method, args);
		};

		return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, handler);
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
		OwnServiceMethod serviceMethod;

		synchronized (serviceMethods) {
			serviceMethod = serviceMethods.get(method);
		}

		if (serviceMethod == null) {
			serviceMethod = new OwnServiceMethod(baseUrl, method);

			synchronized (serviceMethods) {
				serviceMethods.put(method, serviceMethod);
			}
		}

		// Rebuild arguments of service method since args are dynamic
		String requestMethod;
		byte[] body;
		ArrayMap<String, String> headers = new ArrayMap<>();
		String url;
//		Class<?> responseClass;

		synchronized (serviceMethod) {
			serviceMethod.build(method, args);

			requestMethod = serviceMethod.requestMethod;
			body = serviceMethod.body;
			headers.putAll(serviceMethod.headers);
			url = serviceMethod.url;
//			responseClass = serviceMethod.responseClass;
		}

		// Start request to server with parsed info
		return startRequest(requestMethod, body, headers, url);
	}

	private TheHttpResponse startRequest(String requestMethod, byte[] body,
		SimpleArrayMap<String, String> headers, String url) throws Exception {

		DkHttpClient httpClient = new DkHttpClient(url)
			.setReadTimeout(readTimeoutMillis)
			.setConnectTimeout(connectTimeoutMillis)
			.setRequestMethod(requestMethod)
			.setBody(body);

		if (credential != null) {
			httpClient.addToHeader(DkHttpConst.AUTHORIZATION, credential);
		}

		httpClient.addAllToHeader(headers);

		if (DkBuildConfig.DEBUG) {
			DkConsoleLogs.info(this, "Network request at thread: %s", Thread.currentThread().toString());
		}

		return httpClient.execute();
	}
}
