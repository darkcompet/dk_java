/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import tool.compet.core4j.BuildConfig;
import tool.compet.core4j.DkConsoleLogs;
import tool.compet.core4j.DkUtils;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Performs request (GET, POST...) to server. You can specify response type or it will be
 * acted as String, so be converted from Json string to Object.
 * Note that you must do it in IO thread. Here is an example of usage;
 * <code><pre>
 *    DkHttpClient<Bitmap> client = new DkHttpClient<>();
 *    DkHttpResponse<Bitmap> httpResponse = client.execute(imageUrl, Bitmap.class);
 *
 *    int code = httpResponse.code;
 *    String message = httpResponse.message;
 *    Bitmap bitmap = httpResponse.body().bitmap();
 * </pre></code>
 *
 * @param <T> Subclass of this.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DkHttpClient<T extends DkHttpClient> {
	protected final SimpleArrayMap<String, String> headers = new ArrayMap<>();
	protected String requestMethod = DkHttpConst.GET;
	protected String link;
	protected byte[] body;
	protected int connectTimeout = 15000;
	protected int readTimeout = 30000;

	public DkHttpClient() {
	}

	public DkHttpClient(String url) {
		this.link = url;
	}

	public T setUrl(String url) {
		this.link = url;
		return (T) this;
	}

	public T setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
		return (T) this;
	}

	public T addToHeader(String key, String value) {
		headers.put(key, value);
		return (T) this;
	}

	public T addAllToHeader(SimpleArrayMap<String, String> map) {
		headers.putAll(map);
		return (T) this;
	}

	public T setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return (T) this;
	}

	public T setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
		return (T) this;
	}

	public T setBody(byte[] body) {
		this.body = body;
		return (T) this;
	}

	/**
	 * For GET method, we just connect to remote server without decode stream.
	 * For POST method, we write to remote server and wait for client decode stream.
	 */
	public TheHttpResponse execute() throws Exception {
		if (link == null) {
			throw new RuntimeException("Must provide url");
		}
		if (BuildConfig.DEBUG) {
			DkConsoleLogs.info(this, "Start request with link: %s", link);
		}
		final URL url = new URL(link);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		final TheHttpResponse response = new TheHttpResponse(connection);

		for (int index = headers.size() - 1; index >= 0; --index) {
			connection.setRequestProperty(headers.keyAt(index), headers.valueAt(index));
		}
		connection.setConnectTimeout(connectTimeout);
		connection.setReadTimeout(readTimeout);
		connection.setRequestMethod(requestMethod);
		connection.setDoInput(true);

		if (DkHttpConst.GET.equals(requestMethod)) {
			doGet(connection);
		}
		else if (DkHttpConst.POST.equals(requestMethod)) {
			doPost(connection);
		}
		else {
			DkUtils.complainAt(this, "Invalid request method: " + requestMethod);
		}

		return response;
	}

	protected void doGet(HttpURLConnection conn) throws Exception {
		// nothing to do now
	}

	protected void doPost(HttpURLConnection conn) throws Exception {
		if (body != null) {
			conn.setDoOutput(true);
			// Write full post data to body
			BufferedOutputStream os = new BufferedOutputStream(conn.getOutputStream());
			os.write(body);
			os.close();
		}
	}
}
