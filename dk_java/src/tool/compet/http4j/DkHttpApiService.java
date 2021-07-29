/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import tool.compet.core4j.DkUtils;

/**
 * It is convenience class. It will create new instance of http api requester,
 * called as ServiceApi, so caller can use it to request to server.
 * Usage example:
 * <pre>
 *    // Create new instance of UserApi
 *    UserApi userApi = DkApiService.newIns()
 *       .configWith(App.getContext(), "server/server_coresystem.json")
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
public class DkHttpApiService<T extends DkHttpApiService> {
	protected String baseUrl;
	protected String credential;
	protected int connectTimeoutMillis = 15_000; // 15s
	protected int readTimeoutMillis = 30_000; // 30s

	protected final SimpleArrayMap<Method, OwnServiceMethod> serviceMethods;

	public DkHttpApiService() {
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
		validateAndShapingConfig();

		// Create service object from given service class,
		// And register handler on the service object to listen
		// each invocation of methods.
		InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
			// Don't handle method which is not in service class
			if (! method.getDeclaringClass().equals(serviceClass)) {
				return method.invoke(proxy, args);
			}
			// Ok this is api call, execute HTTP request
			return executeHttpRequest(method, args);
		};
		return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[] {serviceClass}, handler);
	}

	private void validateAndShapingConfig() {
		if (baseUrl == null) {
			DkUtils.complainAt(this, "Must specify non-null baseUrl");
		}
		if (! baseUrl.endsWith("/")) {
			baseUrl += '/';
		}
	}

	private TheHttpResponse executeHttpRequest(Method method, Object[] args) throws Exception {
		// For each call of method, we need build HTTP request for it
		// To avoid instantiate multiple times, we will try to cache own service method
		OwnServiceMethod serviceMethod;

		synchronized (serviceMethods) {
			serviceMethod = serviceMethods.get(method);

			// Because we cache this service method, so ONLY pass
			// materials which is fixed (no change more) to it
			if (serviceMethod == null) {
				serviceMethod = new OwnServiceMethod(method);
				serviceMethods.put(method, serviceMethod);
			}
		}

		// Re-build materials of service method since parameter
		// on the method maybe changed each time
		String link;
		String requestMethod;
		SimpleArrayMap<String, String> headers;
		byte[] body;

		synchronized (serviceMethod) {
			// Note that this service method is cached, so we need pass
			// every non-fixed materials to it (fixed materials can be ignored at this time)
			serviceMethod.build(baseUrl, method, args);

			link = serviceMethod.link();
			requestMethod = serviceMethod.requestMethod();
			headers = serviceMethod.headers();
			body = serviceMethod.body();
		}

		// Ok this is time to execute HTTP request
		// with own built service method
		DkHttpClient httpClient = new DkHttpClient(link)
			.setReadTimeout(readTimeoutMillis)
			.setConnectTimeout(connectTimeoutMillis)
			.setRequestMethod(requestMethod)
			.setBody(body);

		if (credential != null) {
			httpClient.addToHeader(DkHttpConst.AUTHORIZATION, credential);
		}
		httpClient.addAllToHeader(headers);

		return httpClient.execute();
	}
}
