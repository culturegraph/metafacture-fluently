package org.culturegraph.mf.joiners;

import java.util.ArrayList;
import java.util.List;

import org.culturegraph.mf.fluently.JoinStrategy;
import org.culturegraph.mf.framework.DefaultStreamPipe;
import org.culturegraph.mf.framework.Sender;
import org.culturegraph.mf.framework.StreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.stream.pipe.IdentityStreamPipe;
import org.culturegraph.mf.stream.pipe.StreamBuffer;

/**
 * @author Christoph Böhme
 */
public class NaiveStreamJoiner implements JoinStrategy<StreamReceiver> {

	private final List<RecordBuffer> buffers = new ArrayList<>();
	private final StreamPipe<StreamReceiver> collector = new IdentityStreamPipe();

	@Override
	public Sender<StreamReceiver> getSender() {
		return collector;
	}

	@Override
	public void addBranch(final Sender<StreamReceiver> sender) {
		final RecordBuffer buffer = new RecordBuffer();
		sender.setReceiver(buffer);
		buffer.setReceiver(collector);
	}

	static class RecordBuffer extends DefaultStreamPipe<StreamReceiver> {

		private final StreamBuffer buffer = new StreamBuffer();

		@Override
		public void startRecord(final String identifier) {
			buffer.startRecord(identifier);
		}

		@Override
		public void endRecord() {
			buffer.endRecord();
			buffer.replay();
			buffer.clear();
		}

		@Override
		public void startEntity(final String name) {
			buffer.startEntity(name);
		}

		@Override
		public void endEntity() {
			buffer.endEntity();
		}

		@Override
		public void literal(final String name, final String value) {
			buffer.literal(name, value);
		}

		@Override
		protected void onSetReceiver() {
			buffer.setReceiver(getReceiver());
		}

	}

}
