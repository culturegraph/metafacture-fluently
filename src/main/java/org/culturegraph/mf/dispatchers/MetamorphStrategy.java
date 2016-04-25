package org.culturegraph.mf.dispatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.culturegraph.mf.fluently.DispatcherStrategy;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.morph.Metamorph;
import org.culturegraph.mf.stream.pipe.StreamBuffer;
import org.culturegraph.mf.stream.sink.SingleValue;

/**
 * @author Christoph Böhme
 */
public class MetamorphStrategy implements DispatcherStrategy<StreamReceiver, String> {

	private final List<ConditionalReceiver> receivers = new ArrayList<>();
	private final MetamorphDispatcher metamorphDispatcher;

	public MetamorphStrategy(final String morph) {
		metamorphDispatcher = new MetamorphDispatcher(morph);
	}

	@Override
	public StreamReceiver getReceiver() {
		return metamorphDispatcher;
	}

	@Override
	public void addBranch(final Predicate<String> condition,
			final StreamReceiver receiver) {
		receivers.add(new ConditionalReceiver(condition, receiver));
	}

	private class ConditionalReceiver {

		final Predicate<String> condition;
		final StreamReceiver receiver;

		private ConditionalReceiver(final Predicate<String> condition,
				final StreamReceiver receiver) {
			this.condition = condition;
			this.receiver = receiver;
		}

	}

	private class MetamorphDispatcher implements StreamReceiver {

		private final StreamBuffer buffer = new StreamBuffer();
		private final SingleValue singleValue = new SingleValue();
		private final Metamorph metamorph;

		public MetamorphDispatcher(final String morph) {
			metamorph = new Metamorph(morph);
			metamorph.setReceiver(singleValue);
		}

		private void dispatch(){
			final String key = singleValue.getValue();
			receivers.stream()
					.filter(condReceiver -> condReceiver.condition.test(key))
					.map(condReceiver -> condReceiver.receiver)
					.forEach(receiver -> {
						buffer.setReceiver(receiver);
						buffer.replay(); });
			buffer.clear();
		}

		@Override
		public void startRecord(final String identifier) {
			buffer.startRecord(identifier);
			metamorph.startRecord(identifier);
		}

		@Override
		public void endRecord() {
			buffer.endRecord();
			metamorph.endRecord();
			dispatch();
		}

		@Override
		public void startEntity(final String name) {
			buffer.startEntity(name);
			metamorph.startEntity(name);
		}

		@Override
		public void endEntity() {
			buffer.endEntity();
			metamorph.endEntity();
		}

		@Override
		public void literal(final String name, final String value) {
			buffer.literal(name, value);
			metamorph.literal(name, value);
		}

		@Override
		public void resetStream() {
			buffer.clear();
			metamorph.resetStream();
			receivers.forEach(condReceiver -> condReceiver.receiver.resetStream());
		}

		@Override
		public void closeStream() {
			buffer.clear();
			metamorph.closeStream();
			receivers.forEach(condReceiver -> condReceiver.receiver.closeStream());
		}

	}

}
