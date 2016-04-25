package org.culturegraph.mf.dispatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.culturegraph.mf.fluently.DispatcherStrategy;
import org.culturegraph.mf.framework.LifeCycle;
import org.culturegraph.mf.framework.StreamReceiver;

/**
 * @author Christoph Böhme
 */
public class DuplicateStreamStrategy implements DispatcherStrategy<StreamReceiver, Void> {

	private final List<StreamReceiver> receivers = new ArrayList<>();
	private final StreamReceiver duplicator = new StreamDuplicator();

	@Override
	public StreamReceiver getReceiver() {
		return duplicator;
	}

	@Override
	public void addBranch(final Predicate<Void> condition,
			final StreamReceiver receiver) {
		receivers.add(receiver);
	}

	private class StreamDuplicator implements StreamReceiver {

		@Override
		public void startRecord(final String id) {
			receivers.forEach(receiver -> receiver.startRecord(id));
		}

		@Override
		public void endRecord() {
			receivers.forEach(StreamReceiver::endRecord);
		}

		@Override
		public void startEntity(final String name) {
			receivers.forEach(receiver -> receiver.startEntity(name));
		}

		@Override
		public void endEntity() {
			receivers.forEach(StreamReceiver::endEntity);
		}

		@Override
		public void literal(final String name, final String value) {
			receivers.forEach(receiver -> receiver.literal(name, value));
		}

		@Override
		public void resetStream() {
			receivers.forEach(LifeCycle::resetStream);
		}

		@Override
		public void closeStream() {
			receivers.forEach(LifeCycle::closeStream);
		}

	}

}
