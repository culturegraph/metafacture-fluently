package org.culturegraph.mf.fluently;

import java.util.function.Predicate;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class TerminatedFlowDispatcher<R extends Receiver, S extends Receiver, P> {

	private final R flowStart;
	private final DispatcherStrategy<S, P> dispatcherStrategy;

	TerminatedFlowDispatcher(
			final FlowDispatcherStart<R, S, P> flowDispatcherStart) {
		flowStart = flowDispatcherStart.flowStart;
		dispatcherStrategy = flowDispatcherStart.dispatcherStrategy;
	}

	public TerminatedFlowDispatcher<R, S, P> to(
			final TerminatingModule<S> flowElement) {
		return to(x -> true, flowElement);
	}

	public TerminatedFlowDispatcher<R, S, P> to(final Predicate<P> condition,
			final TerminatingModule<S> flowElement) {
		dispatcherStrategy.addBranch(condition, flowElement.getReceiver());
		return this;
	}

	public TerminatedFlow<R> terminate() {
		return new TerminatedFlow<>(flowStart);
	}

}
