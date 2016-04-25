package org.culturegraph.mf.fluently;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.culturegraph.mf.framework.DefaultObjectReceiver;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
public class BoundedFlow<T, U> {

	private final FlowRunner<T> flowRunner;
	private final Sender<ObjectReceiver<U>> flowEnd;

	BoundedFlow(final Supplier<T> inputs,
			final Flow<ObjectReceiver<T>, ObjectReceiver<U>> flow) {
		flowRunner = new FlowRunner<>(inputs, flow.getReceiver());
		flowEnd = flow.getSender();
	}

	public <A, R> R collectResults(final Collector<? super U, A, R> collector) {
		final BiConsumer<A, ? super U> accumulator = collector.accumulator();
		final A collection = collector.supplier().get();
		flowEnd.setReceiver(new DefaultObjectReceiver<U>() {
			@Override
			public void process(final U object) {
				accumulator.accept(collection, object);
			}
		});
		flowRunner.processAll();
		return collector.finisher().apply(collection);
	}

	public Stream<U> streamResults() {
		return StreamSupport.stream(new FlowSpliterator<>(flowRunner, flowEnd), false);
	}

	public void discardResults() {
		flowEnd.setReceiver(new DefaultObjectReceiver<>());
		flowRunner.processNextValue();
	}

}
