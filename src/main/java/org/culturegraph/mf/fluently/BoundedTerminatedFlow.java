package org.culturegraph.mf.fluently;

import java.util.function.Supplier;

import org.culturegraph.mf.framework.ObjectReceiver;

/**
 * @author Christoph Böhme
 */
public class BoundedTerminatedFlow<T> {

	private final FlowRunner<T> flowRunner;

	BoundedTerminatedFlow(final Supplier<T> inputs,
			final TerminatedFlow<ObjectReceiver<T>> flow) {
		flowRunner = new FlowRunner<>(inputs, flow.getReceiver());
	}

	public void run() {
		flowRunner.processAll();
	}

}
