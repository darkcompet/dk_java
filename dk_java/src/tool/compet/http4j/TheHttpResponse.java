/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Response of a request. It extracts some basic information from response
 * as code, message, body...
 */
public class TheHttpResponse {
	// Connection for read stream, must close after decode input
	protected final HttpURLConnection connection;

	// Http status code: HttpURLConnection.*
	protected int code = -1;

	// Http Message
	protected String message;

	// Content from server
	protected TheHttpResponseBody body;

	protected TheHttpResponse(HttpURLConnection connection) {
		this.connection = connection;
	}

	/**
	 * For more detail information, use this.
	 */
	public HttpURLConnection connection() {
		return connection;
	}

	public int code() throws IOException {
		return code != -1 ? code : (code = connection.getResponseCode());
	}

	public String message() throws IOException {
		return message != null ? message : (message = connection.getResponseMessage());
	}

	public TheHttpResponseBody body() {
		return body != null ? body : (body = new TheHttpResponseBody(connection));
	}
}
