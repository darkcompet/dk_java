/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Attach this to a method to make with GET request method.
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface DkHttpGetRequest {
	/**
	 * @return relative url of api, for eg,. user/1/profile
	 */
	String value();

	/**
	 * @return Get data format, for eg,. application/json
	 */
	String contentType() default DkHttpConst.APPLICATION_JSON;
}
