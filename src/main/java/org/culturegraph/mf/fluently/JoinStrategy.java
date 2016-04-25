package org.culturegraph.mf.fluently;

import org.culturegraph.mf.framework.Receiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
public interface JoinStrategy<R extends Receiver> {

	Sender<R> getSender();

	void addBranch(final Sender<R> sender);  // TODO: Rename to addSender?

}
