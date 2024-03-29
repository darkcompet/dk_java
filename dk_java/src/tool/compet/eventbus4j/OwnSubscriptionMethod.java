/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.eventbus4j;

import java.lang.reflect.Method;

import tool.compet.core4j.DkUtils;

public class OwnSubscriptionMethod {
	public final Method method;
	public Class<?> paramType;
	public final int id;
	public final int priority;
	public final int threadMode;
	public final boolean sticky;
	public final boolean allowNullableParam;

	public OwnSubscriptionMethod(Method method) {
		Class[] paramTypes = method.getParameterTypes();

		if (paramTypes.length > 0) {
			this.paramType = (Class<?>) paramTypes[0];
		}

		if (paramType == null || paramType.isPrimitive()) {
			DkUtils.complainAt(this, "Require non-primitive parameter.");
		}

		DkSubscribe subscription = method.getAnnotation(DkSubscribe.class);
		if (subscription == null) {
			throw new RuntimeException("Oops !");
		}
		this.method = method;
		this.id = subscription.id();
		this.priority = subscription.priority();
		this.threadMode = subscription.threadMode();
		this.sticky = subscription.sticky();
		this.allowNullableParam = subscription.allowNullParam();
	}
}
