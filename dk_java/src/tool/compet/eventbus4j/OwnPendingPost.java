/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.eventbus4j;

public class OwnPendingPost {
	public OwnSubscription subscription;
	public Object event;

	public OwnPendingPost next;

	public OwnPendingPost(OwnSubscription subscription, Object event) {
		this.subscription = subscription;
		this.event = event;
	}
}
