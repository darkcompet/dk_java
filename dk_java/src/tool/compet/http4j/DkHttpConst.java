/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

public interface DkHttpConst {
	// Http request methods
	String GET = "GET";
	String POST = "POST";
	String HEAD = "HEAD";
	String OPTIONS = "OPTIONS";
	String PUT = "PUT";
	String DELETE = "DELETE";
	String TRACE = "TRACE";

	// Authentication method
	String AUTHORIZATION = "Authorization";
	String BASIC_AUTH = "Basic ";
	String BEARER_AUTH = "Bearer ";

	// Content Format
	String CONTENT_TYPE = "Content-Type";
	String ACCEPT = "Accept";
	String APPLICATION_JSON = "application/json";
	String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
}
