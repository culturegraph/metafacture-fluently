package org.culturegraph.mf.fluently;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class TerminatedFlow<R extends Receiver>
		implements TerminatingModule<R> {

	private final R firstReceiver;

	TerminatedFlow(final R firstReceiver) {
		this.firstReceiver = firstReceiver;
	}

	@Override
	public R getReceiver() {
		return firstReceiver;
	}

}
