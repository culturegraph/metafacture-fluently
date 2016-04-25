package org.culturegraph.mf.fluently;

import org.culturegraph.mf.framework.Receiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
public class Flow<R extends Receiver, S extends Receiver>
		implements Module<R, S> {

	private final R firstReceiver;
	private final Sender<S> lastSender;

	Flow(final R firstReceiver, final Sender<S> lastSender) {
		this.firstReceiver = firstReceiver;
		this.lastSender = lastSender;
	}

	@Override
	public R getReceiver() {
		return firstReceiver;
	}

	@Override
	public Sender<S> getSender() {
		return lastSender;
	}

	public <N extends Receiver> Flow<R, N> followedBy(final Module<S, N> module) {
		lastSender.setReceiver(module.getReceiver());
		return new Flow<>(firstReceiver, module.getSender());
	}

	public TerminatedFlow<R> followedBy(final TerminatingModule<S> module) {
		lastSender.setReceiver(module.getReceiver());
		return new TerminatedFlow<>(firstReceiver);
	}

	public <P> FlowDispatcherStart<R, S, P> dispatchWith(
			final DispatcherStrategy<S, P> dispatcherStrategy) {
		lastSender.setReceiver(dispatcherStrategy.getReceiver());
		return new FlowDispatcherStart<>(firstReceiver, dispatcherStrategy);
	}

}
