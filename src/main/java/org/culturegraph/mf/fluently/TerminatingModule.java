package org.culturegraph.mf.fluently;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public interface TerminatingModule<R extends Receiver> {

	R getReceiver();

}
