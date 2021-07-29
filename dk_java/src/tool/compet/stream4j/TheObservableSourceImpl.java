/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import androidx.annotation.NonNull;

public abstract class TheObservableSourceImpl<M> implements DkObservableSource<M> {
	// Parent node (upper stream) of this node
	// For God node, this field will be null
	protected DkObservableSource<M> parent;

	// This field (pointer) is presented in and point to God node at initialized time of God node.
	// After create a new child node which refers to God node, this
	// will point to child node, that is, this always point to tail node.
	protected DkObservableSource<M> tail;

	/**
	 * Subscribe with empty (leaf) observer (listener, callback) to stream.
	 */
	@Override
	public void subscribe() {
		subscribe(new OwnLeafObserver<>());
	}

	/**
	 * Subscribe with an observer (listener, callback) to stream, we can listen what happen in the stream.
	 *
	 * @param observer For first time, this is object passed from caller. Next, we wrap it to own observer
	 *                 and pass up to parent node.
	 */
	@Override
	public void subscribe(@NonNull DkObserver<M> observer) {
		try {
			// Go up to send observer (normally wrapped observer) of this node to parent node,
			// by do it, we can make linked list of observers, so parent can pass events to this node later.
			if (tail == null) { // intermediate node
				subscribeActual(observer);
			}
			else { // this is God node
				// Note that: different with normal node, the God node contains non-null tail pointer !!!
				// So we must unlink pointer `God.tail` before call to parent node.
				// since parent will call parent of it again, so until reach to God node,
				// the God node will use this tail to call tail node again, which causes
				// stackoverflow problem.
				DkObservableSource<M> tail = this.tail;
				this.tail = null;
				tail.subscribeActual(observer);
			}
		}
		catch (Exception e) {
			// Unable to subscribe (make node-link process), just pass error and then final events
			// to child observer
			observer.onError(e);
			observer.onFinal();
		}
	}
}
