package org.culturegraph.mf.fluently;

import java.util.function.Predicate;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public class FlowDispatcherStart<R extends Receiver, S extends Receiver, P> {

	final R flowStart;
	final DispatcherStrategy<S, P> dispatcherStrategy;

	FlowDispatcherStart(final R flowStart,
			final DispatcherStrategy<S, P> dispatcherStrategy) {
		this.flowStart = flowStart;
		this.dispatcherStrategy = dispatcherStrategy;
	}

	public <N extends Receiver> FlowDispatcher<R, S, N, P> to(
			final Module<S, N> module) {
		return to(x -> true, module);
	}

	public <N extends Receiver> FlowDispatcher<R, S, N, P> to(
			final Predicate<P> condition, final Module<S, N> module) {
		final FlowDispatcher<R, S, N, P> flowDispatcher = new FlowDispatcher<>(this);
		return flowDispatcher.to(condition, module);
	}

	public TerminatedFlowDispatcher<R, S, P> to(
			final TerminatingModule<S> flowElement) {
		return to(x -> true, flowElement);
	}

	public TerminatedFlowDispatcher<R, S, P> to(final Predicate<P> condition,
			final TerminatingModule<S> flowElement) {
		final TerminatedFlowDispatcher<R, S, P> flowDispatcher =
				new TerminatedFlowDispatcher<>(this);
		return flowDispatcher.to(condition, flowElement);
	}

}
