package org.culturegraph.mf.fluently;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.culturegraph.mf.framework.DefaultObjectReceiver;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
class FlowSpliterator<T, U> implements Spliterator<U> {

	private final FlowRunner<T> flowRunner;

	private final Deque<U> queue = new ArrayDeque<>();

	public FlowSpliterator(final FlowRunner<T> flowRunner,
			final Sender<ObjectReceiver<U>> flowEnd) {
		this.flowRunner = flowRunner;
		setQueueReceiver(flowEnd);
	}

	private void setQueueReceiver(final Sender<ObjectReceiver<U>> flowEnd) {
		flowEnd.setReceiver(new DefaultObjectReceiver<U>() {
			@Override
			public void process(final U object) {
				queue.addLast(object);
			}
		});
	}

	@Override
	public boolean tryAdvance(final Consumer<? super U> action) {
		if (queue.isEmpty()) {
			flowRunner.processNextValue();
		}
		if (!queue.isEmpty()) {
			action.accept(queue.removeFirst());
			return true;
		}
		return false;
	}

	@Override
	public Spliterator<U> trySplit() {
		return null;
	}

	@Override
	public long estimateSize() {
		return Long.MAX_VALUE;
	}

	@Override
	public int characteristics() {
		return IMMUTABLE | ORDERED;
	}

}
