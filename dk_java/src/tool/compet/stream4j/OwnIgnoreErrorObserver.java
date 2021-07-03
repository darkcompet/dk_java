/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.stream4j;

public class OwnIgnoreErrorObserver<M> extends OwnObserver<M> {
	public OwnIgnoreErrorObserver(DkObserver<M> child) {
		super(child);
	}

	@Override
	public void onError(Throwable e) {
		try {
			// We don't know what result was passed from us parent node
			// -> Ignore error and try to pass with null-result to child node
			child.onNext(null);
		}
		catch (Exception exception) {
			// Humh, still error occured in child node...
			// -> We have no idea to resolve it
			// -> Just pass to child node exception which was caused from it
			child.onError(exception);
		}
	}
}