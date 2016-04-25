package org.culturegraph.mf.fluently;

import org.culturegraph.mf.framework.Receiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
class LegacyModuleWrapper<R extends Receiver, S extends Receiver>
		implements Module<R, S> {

	private final R receiver;
	private final Sender<S> sender;

	LegacyModuleWrapper(final R receiver, final Sender<S> sender) {
		this.receiver = receiver;
		this.sender = sender;
	}

	@Override
	public R getReceiver() {
		return receiver;
	}

	@Override
	public Sender<S> getSender() {
		return sender;
	}

}
