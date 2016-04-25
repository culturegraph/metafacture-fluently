package org.culturegraph.mf.fluently;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
class TerminatingLegacyModuleWrapper<R extends Receiver>
		implements TerminatingModule<R> {

	private final R receiver;

	TerminatingLegacyModuleWrapper(final R receiver) {
		this.receiver = receiver;
	}

	@Override
	public R getReceiver() {
		return receiver;
	}

}
