/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.eventbus4j;

import java.lang.reflect.InvocationTargetException;

public class OwnSubscription {
	public final Object subscriber;
	public final OwnSubscriptionMethod subscriptionMethod;

	// Become false if this subscription is unregistered
	public boolean active = true;

	public OwnSubscription(Object subscriber, OwnSubscriptionMethod subscriptionMethod) {
		this.subscriber = subscriber;
		this.subscriptionMethod = subscriptionMethod;
	}

	public void invoke(Object param) throws InvocationTargetException, IllegalAccessException {
		OwnSubscriptionMethod sm = subscriptionMethod;

		if (param == null ? sm.allowNullableParam : sm.paramType.isAssignableFrom(param.getClass())) {
			sm.method.invoke(subscriber, param);
		}
	}
}
