/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.eventbus4j;

public class OwnPendingPostQueue {
	private OwnPendingPost head;
	private OwnPendingPost tail;

	public void enqueue(OwnPendingPost pp) {
		if (tail != null) {
			tail.next = tail = pp;
		}
		else if (head == null) {
			head = tail = pp;
		}
		else {
			throw new RuntimeException("Invalid state, head: " + head + ", tail: " + tail);
		}
	}

	public OwnPendingPost dequeue() {
		OwnPendingPost h = head;

		if (h != null) {
			OwnPendingPost next = h.next;
			head = next;

			h.next = null;

			if (next == null) {
				tail = null;
			}
		}

		return h;
	}
}
