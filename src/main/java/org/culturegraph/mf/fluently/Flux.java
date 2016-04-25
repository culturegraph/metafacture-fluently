package org.culturegraph.mf.fluently;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

import org.culturegraph.mf.framework.ObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.Receiver;
import org.culturegraph.mf.framework.StreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.framework.XmlPipe;
import org.culturegraph.mf.framework.XmlReceiver;

/**
 * @author Christoph Böhme
 */
public class Flux {

	// TODO: Is this really safe?
	@SafeVarargs
	public  static <T> FlowInputs<T> process(final T... input) {
		return process(asList(input));
	}

	public static <T> FlowInputs<T> process(final Collection<T> inputs) {
		final Iterator<T> inputIterator = inputs.iterator();
		return process(() -> {
			return inputIterator.hasNext()
					? inputIterator.next()
					: null;
			});
	}

	public static <T> FlowInputs<T> process(final Supplier<T> supplier) {
		return new FlowInputs<>(supplier);
	}

	public static <U extends Receiver> Module<StreamReceiver, U> module(
			final StreamPipe<U> streamPipe) {
		return new LegacyModuleWrapper<>(streamPipe, streamPipe);
	}

	public static <T, U extends Receiver> Module<ObjectReceiver<T>, U> module(
			final ObjectPipe<T, U> objectPipe) {
		return new LegacyModuleWrapper<>(objectPipe, objectPipe);
	}

	public static <U extends Receiver> Module<XmlReceiver, U> module(
			final XmlPipe<U> xmlPipe) {
		return new LegacyModuleWrapper<>(xmlPipe, xmlPipe);
	}

	public static <R extends Receiver> TerminatingModule<R> terminatingModule(
			final R receiver) {
		return new TerminatingLegacyModuleWrapper<>(receiver);
	}

	public static <R extends Receiver, S extends Receiver> Flow<R, S> flowWith(
			final Module<R, S> module)  {
		return new Flow<>(module.getReceiver(), module.getSender());
	}

	public static <R extends Receiver> TerminatedFlow<R> flowWith(
			final TerminatingModule<R> module) {
		return new TerminatedFlow<>(module.getReceiver());
	}

	public static <R extends Receiver, P> FlowDispatcherStart<R, R, P>
	dispatchWith(final DispatcherStrategy<R, P> dispatcherStrategy) {
		return new FlowDispatcherStart<>(dispatcherStrategy.getReceiver(),
				dispatcherStrategy);
	}

}
