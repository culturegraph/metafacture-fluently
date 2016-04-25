package org.culturegraph.mf.joiners;

import org.culturegraph.mf.fluently.JoinStrategy;
import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.ObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
public class NaiveObjectsJoiner<T> implements JoinStrategy<ObjectReceiver<T>> {

	private ObjectPipe<T, ObjectReceiver<T>> passThrough = new IdentityObjectPipe();

	@Override
	public Sender<ObjectReceiver<T>> getSender() {
		return passThrough;
	}

	@Override
	public void addBranch(final Sender<ObjectReceiver<T>> sender) {
		sender.setReceiver(passThrough);
	}

	private class IdentityObjectPipe extends DefaultObjectPipe<T, ObjectReceiver<T>> {

		@Override
		public void process(final T object) {
			getReceiver().process(object);
		}

	}

}
