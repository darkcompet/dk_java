/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.http4j;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Use this for body which be sent to server. Body type will be: Primitive, String, Bitmap
 * and Object which can be converted to Json string.
 */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface DkBody {
}
