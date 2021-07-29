/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

import java.util.concurrent.TimeUnit;

import tool.compet.core4j.DkCallable1;
import tool.compet.core4j.DkRunnable;
import tool.compet.core4j.DkRunnable1;

/**
 * This provides chained-method calling, called as stream or callback-system.
 * For detail, refer to RxJava or Java stream at https://github.com/ReactiveX/RxJava
 *
 * @param <M> Model which be passed down from parent node to child node.
 */
@SuppressWarnings("unchecked")
public abstract class DkObservable<M> extends TheObservableSourceImpl<M> {
	// Don't public
	protected DkObservable() {
	}

	// Don't public
	protected DkObservable(DkObservableSource<M> parent) {
		this.parent = parent;
	}

	/**
	 * This map is used to switch model type.
	 */
	public <N> DkObservable<N> map(DkCallable1<M, N> function) {
		tail = ((DkObservableSource<M>) new OwnMapObservable<>(tail, function));
		return (DkObservable<N>) this;
	}

	/**
	 * This switched model type, is like with `map()`, but this maps with other stream.
	 */
	public <N> DkObservable<N> flatMap(DkCallable1<M, DkObservableSource<N>> function) {
		tail = ((DkObservableSource<M>) new OwnMapStreamObservable<>(tail, function));
		return (DkObservable<N>) this;
	}

	public DkObservable<M> ignoreError() {
		tail = new OwnIgnoreErrorObservable<>(tail);
		return this;
	}

	public abstract DkObservable<M> delay(long duration);

	public abstract DkObservable<M> delay(long duration, TimeUnit unit);

	public DkObservable<M> scheduleInBackground() {
		return scheduleIn(DkSchedulers.io(), 0, TimeUnit.MILLISECONDS, false);
	}

	public DkObservable<M> scheduleIn(DkScheduler<M> scheduler) {
		return scheduleIn(scheduler, 0, TimeUnit.MILLISECONDS, false);
	}

	public DkObservable<M> scheduleIn(DkScheduler<M> scheduler, boolean isSerial) {
		return scheduleIn(scheduler, 0, TimeUnit.MILLISECONDS, isSerial);
	}

	public DkObservable<M> scheduleIn(DkScheduler<M> scheduler, long delay, TimeUnit unit, boolean isSerial) {
		tail = new OwnScheduleInObservable<>(tail, scheduler, delay, unit, isSerial);
		return this;
	}

	public DkObservable<M> observeOn(DkScheduler<M> scheduler) {
		return observeOn(scheduler, 0L, TimeUnit.MILLISECONDS, true);
	}

	public DkObservable<M> observeOn(DkScheduler<M> scheduler, long delayMillis) {
		return observeOn(scheduler, delayMillis, TimeUnit.MILLISECONDS, true);
	}

	public DkObservable<M> observeOn(DkScheduler<M> scheduler, long delay, TimeUnit unit, boolean isSerial) {
		tail = new OwnThreadSwitcherObservable<>(tail, scheduler, null, delay, unit, isSerial);
		return this;
	}

	public abstract DkObservable<M> observeOnForeground();

	public abstract DkObservable<M> scheduleInBackgroundAndObserveOnForeground();

	public DkObservable<M> switchThread(DkScheduler<M> scheduler, DkRunnable1<M> action) {
		return switchThread(scheduler, action, 0, TimeUnit.MILLISECONDS, true);
	}

	/**
	 * Make flow run in other thread, this is thread-switching.
	 */
	public DkObservable<M> switchThread(DkScheduler<M> scheduler, DkRunnable1<M> action, long delay, TimeUnit unit, boolean isSerial) {
		tail = new OwnThreadSwitcherObservable<>(tail, scheduler, action, delay, unit, isSerial);
		return this;
	}

	/**
	 * Hears subscribe-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public DkObservable<M> doOnSubscribe(DkRunnable1<DkControllable> action) {
		tail = new OwnOnSubscribeObservable<>(tail, action);
		return this;
	}

	/**
	 * Hears success-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public DkObservable<M> doOnNext(DkRunnable1<M> action) {
		tail = new OwnOnNextObservable<>(tail, action);
		return this;
	}

	/**
	 * Hears error-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public DkObservable<M> doOnError(DkRunnable1<Throwable> action) {
		tail = new OwnOnErrorObservable<>(tail, action);
		return this;
	}

	/**
	 * Hears complete-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public DkObservable<M> doOnComplete(DkRunnable action) {
		tail = new OwnOnCompleteObservable<>(tail, action);
		return this;
	}

	/**
	 * Hears final-event while streaming. Note that, this method is developed to make observe
	 * stream-events easier when subscribing, so equivalent to #subscribe(observer),
	 * this function does not affect flow of current stream even if action throws exception.
	 */
	public DkObservable<M> doOnFinal(DkRunnable action) {
		tail = new OwnOnFinalObservable<>(tail, action);
		return this;
	}

	public void subscribeAsync() {
		this.scheduleInBackground().subscribe();
	}

	public DkControllable<M> subscribeForControllable() {
		return subscribeForControllable(new DkControllable<>(new OwnLeafObserver<>()));
	}

	/**
	 * Subscribe a observer (listener, callback) to stream, so we can listen what happening in stream.
	 * Differ with aother subscribe() method, this will return Controllable object,
	 * so you can control (dispose, resume, pause...) stream anytime you want.
	 */
	public DkControllable<M> subscribeForControllable(DkObserver<M> observer) {
		DkControllable<M> controllable = new DkControllable<>(observer);
		subscribe(controllable);
		return controllable;
	}
}
