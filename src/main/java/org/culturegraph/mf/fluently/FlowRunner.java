package org.culturegraph.mf.fluently;

import java.util.function.Supplier;

import org.culturegraph.mf.framework.ObjectReceiver;

/**
 * @author Christoph Böhme
 */
class FlowRunner<T> {

	private final Supplier<T> inputs;
	private final ObjectReceiver<T> flowStart;

	FlowRunner(final Supplier<T> inputs,
			final ObjectReceiver<T> flowStart) {
		this.inputs = inputs;
		this.flowStart = flowStart;
	}

	void processAll() {
		while(processNextValue());
	}

	boolean processNextValue() {
		final T input = inputs.get();
		if (input != null) {
			flowStart.process(input);
			return true;
		}
		return false;
	}

}
