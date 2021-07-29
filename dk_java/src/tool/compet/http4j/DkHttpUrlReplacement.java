/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Use this to replace an element in the url that specified in RequestMethod (GET, POST...).
 */
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
public @interface DkHttpUrlReplacement {
	/**
	 * @return name which matches with some node on the url path.
	 */
	String value();
}
