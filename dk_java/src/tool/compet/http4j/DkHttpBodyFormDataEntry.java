/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Use this annotation on params of the method.
 * When request with POST method, we will convert all body entries
 * to format `k1=v1&k2=v2...` and send it as bytes to server after headers.
 */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface DkHttpBodyFormDataEntry {
	/**
	 * @return Name of form-data entry.
	 */
	String value();
}
