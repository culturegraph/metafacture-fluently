package org.culturegraph.mf.fluently;

import org.culturegraph.mf.framework.Receiver;
import org.culturegraph.mf.framework.Sender;

/**
 * @author Christoph Böhme
 */
public interface Module<R extends Receiver, S extends Receiver> {

	R getReceiver();

	Sender<S> getSender();

}
