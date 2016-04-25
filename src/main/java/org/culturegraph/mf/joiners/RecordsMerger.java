package org.culturegraph.mf.joiners;

import org.culturegraph.mf.fluently.JoinStrategy;
import org.culturegraph.mf.framework.DefaultStreamPipe;
import org.culturegraph.mf.framework.Sender;
import org.culturegraph.mf.framework.StreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;

/**
 * @author Christoph Böhme
 */
public class RecordsMerger implements JoinStrategy<StreamReceiver> {

	private final StreamPipe<StreamReceiver> merger = new RecordsMergerPipe();

	private int branchCount;

	@Override
	public Sender<StreamReceiver> getSender() {
		return merger;
	}

	@Override
	public void addBranch(final Sender<StreamReceiver> sender) {
		sender.setReceiver(merger);
		branchCount++;
	}

	private class RecordsMergerPipe extends DefaultStreamPipe<StreamReceiver> {

		private int startRecordCount;
		private int endRecordCount;

		@Override
		public void startRecord(final String identifier) {
			if (startRecordCount <= 0) {
				getReceiver().startRecord(identifier);
				endRecordCount = branchCount;
			}
			startRecordCount++;
			if (startRecordCount >= branchCount) {
				startRecordCount = 0;
			}
		}

		@Override
		public void endRecord() {
			endRecordCount--;
			if (endRecordCount <= 0) {
				getReceiver().endRecord();
			}
		}

		@Override
		public void startEntity(final String name) {
			getReceiver().startEntity(name);
		}

		@Override
		public void endEntity() {
			getReceiver().endEntity();
		}

		@Override
		public void literal(final String name, final String value) {
			getReceiver().literal(name, value);
		}

	}

}
