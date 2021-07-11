/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.eventbus4j;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import tool.compet.core.DkLogcats;

class MyMainPoster implements Handler.Callback {
	private final DkEventBus eventbus;
	private final OwnPendingPostQueue queue;
	private final Handler handler;
	private volatile boolean isRunning;

	MyMainPoster(DkEventBus eventbus) {
		this.eventbus = eventbus;
		this.queue = new OwnPendingPostQueue();
		this.handler = new Handler(Looper.getMainLooper(), this);
	}

	void enqueue(OwnSubscription subscription, Object event) {
		OwnPendingPost pendingPost = new OwnPendingPost(subscription, event);

		synchronized (this) {
			queue.enqueue(pendingPost);

			if (! isRunning) {
				isRunning = true;

				if (! handler.sendMessageDelayed(Message.obtain(handler), 0)) {
					// give a change to try again
					isRunning = false;
					DkLogcats.warning(this, "Could not send handler message");
				}
			}
		}
	}

	@Override
	public boolean handleMessage(@NonNull Message msg) {
		long start = System.currentTimeMillis();
		OwnPendingPost pendingPost;

		while (true) {
			synchronized (queue) {
				pendingPost = queue.dequeue();
			}

			if (pendingPost == null) {
				synchronized (this) {
					pendingPost = queue.dequeue();

					if (pendingPost == null) {
						isRunning = false;
						break;
					}
				}
			}

			eventbus.invokeSubscriber(pendingPost.subscription, pendingPost.event);

			// Because Android framework maybe skip frames which coming too close,
			// so we Only request next message if elapsed time over actual frameDelay (10ms)
			if (System.currentTimeMillis() - start > 10) {
				if (! handler.sendMessageDelayed(Message.obtain(handler), 0)) {
					// give a change to try send message again
					isRunning = false;
					DkLogcats.warning(this, "Could not send handler message again");
				}
				break;
			}
		}

		return true;
	}
}
