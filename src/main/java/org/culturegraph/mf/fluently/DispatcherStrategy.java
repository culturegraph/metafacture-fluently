package org.culturegraph.mf.fluently;

import java.util.function.Predicate;

import org.culturegraph.mf.framework.Receiver;

/**
 * @author Christoph Böhme
 */
public interface DispatcherStrategy<R extends Receiver, P> {

	R getReceiver();

	void addBranch(final Predicate<P> condition, R receiver);  // TODO: Rename to addReceiver?

}
