package org.culturegraph.mf.fluently;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.culturegraph.mf.framework.Receiver;

public class FlowDispatcher<F extends Receiver, L extends Receiver, N extends Receiver, P> {

	private final F flowStart;
	private final DispatcherStrategy<L, P> dispatcherStrategy;
	private final List<Module<L, N>> branches = new ArrayList<>();

	FlowDispatcher(final FlowDispatcherStart<F, L, P> flowDispatcherStart) {
		this.flowStart = flowDispatcherStart.flowStart;
		this.dispatcherStrategy = flowDispatcherStart.dispatcherStrategy;
	}

	public FlowDispatcher<F, L, N, P> to(final Module<L, N> module) {
		return to(x -> true, module);
	}

	public FlowDispatcher<F, L, N, P> to(final Predicate<P> condition,
			final Module<L, N> module) {
		dispatcherStrategy.addBranch(condition, module.getReceiver());
		branches.add(module);
		return this;
	}

	public Flow<F, N> joinWith(final JoinStrategy<N> joinStrategy) {
		branches.forEach(branch -> joinStrategy.addBranch(branch.getSender()));
		return new Flow<>(flowStart, joinStrategy.getSender());
	}

}
