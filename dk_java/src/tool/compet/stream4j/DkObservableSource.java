/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

public interface DkObservableSource<M> {
	/**
	 * Subscribe stream without observer, so the stream should generate default observer.
	 */
	void subscribe();

	/**
	 * Subscribe stream with an observer.
	 */
	void subscribe(DkObserver<M> observer);

	/**
	 * Note for implementation time
	 * <ul>
	 *    <li>
	 *       For God node: implement logic of emitting events (#onNext, #onError, #onFinal...) to under node.
	 *       The code should be blocked by try-catch to call #onFinal event.
	 *    </li>
	 *    <li>
	 *       For Godless node: just wrap given child observer and send to the upper node.
	 *    </li>
	 * </ul>
	 * The remain work to do is, write code in event-methods of Godless node.
	 * This job is same with implementation logic of God node. Mainly job is writing logic of #onNext,
	 * and if sometimes exception raised, you can call #onError to notify to lower node.
	 *
	 * @throws Exception When unable to subscribe at this node.
	 */
	void subscribeActual(DkObserver<M> observer) throws Exception;
}
