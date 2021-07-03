/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Use this on a param to make it as body which be sent to server.
 * Body type must be: byte[].
 */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface DkBody {
}
