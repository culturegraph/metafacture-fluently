package org.culturegraph.mf.fluently;

import java.util.function.Supplier;

import org.culturegraph.mf.framework.ObjectReceiver;

/**
 * @author Christoph Böhme
 */
public class FlowInputs<T> {

	private final Supplier<T> supplier;

	FlowInputs(final Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public <U> BoundedFlow<T, U> with(
			final Flow<ObjectReceiver<T>, ObjectReceiver<U>> flow) {
		return new BoundedFlow<>(supplier, flow);
	}

	public BoundedTerminatedFlow<T> with(final TerminatedFlow<ObjectReceiver<T>> flow) {
		return new BoundedTerminatedFlow<>(supplier, flow);
	}

}
