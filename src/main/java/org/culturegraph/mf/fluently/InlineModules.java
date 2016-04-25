package org.culturegraph.mf.fluently;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.DefaultObjectReceiver;
import org.culturegraph.mf.framework.DefaultStreamPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.StreamReceiver;

/**
 * @author Christoph Böhme
 */
public class InlineModules {

	private InlineModules() {
		// no instances allowed
	}

	public static <T> Module<ObjectReceiver<T>, StreamReceiver> streamMap(
			final BiConsumer<T, StreamReceiver> function) {
		return Flux.module(new DefaultObjectPipe<T, StreamReceiver>() {
			@Override
			public void process(final T object) {
				function.accept(object, getReceiver());
			}
		});
	}

	public static <T, U> Module<ObjectReceiver<T>, ObjectReceiver<U>> flatMap(
			final Function<T, Iterable<U>> function) {
		return Flux.module(new DefaultObjectPipe<T, ObjectReceiver<U>>() {
			@Override
			public void process(final T object) {
				function.apply(object).forEach(getReceiver()::process);
			}
		});
	}

	public static <T, U> Module<ObjectReceiver<T>, ObjectReceiver<U>> map(
			final Function<T, U> function) {
		return Flux.module(new DefaultObjectPipe<T, ObjectReceiver<U>>() {
			@Override
			public void process(final T object) {
				getReceiver().process(function.apply(object));
			}
		});
	}

	public static Module<StreamReceiver, StreamReceiver> mapLiterals(
			final Function<Literal, Literal> function) {
		return Flux.module(new ForwardingStreamPipe() {
			@Override
			public void literal(final String name, final String value) {
				final Literal mappedLiteral = function.apply(new Literal(name, value));
				getReceiver().literal(mappedLiteral.name, mappedLiteral.value);
			}
		});
	}

	public static Module<StreamReceiver, StreamReceiver> mapLiterals(
			final BiFunction<String, String, String> function) {
		return Flux.module(new ForwardingStreamPipe() {
			@Override
			public void literal(final String name, final String value) {
				getReceiver().literal(name, function.apply(name, value));
			}
		});
	}

	public static Module<StreamReceiver, StreamReceiver> streamMapLiterals(
			final BiConsumer<Literal, StreamReceiver> consumer) {
		return Flux.module(new ForwardingStreamPipe() {
			@Override
			public void literal(final String name, final String value) {
				consumer.accept(new Literal(name, value), getReceiver());
			}
		});
	}

	public static Module<StreamReceiver, StreamReceiver> mapRecordIds(
			final Function<String, String> function) {
		return Flux.module(new ForwardingStreamPipe() {
			@Override
			public void startRecord(final String identifier) {
				getReceiver().startRecord(function.apply(identifier));
			}
		});
	}

	public static <T> TerminatingModule<ObjectReceiver<T>> consume(
			final Consumer<T> consumer) {
		return Flux.terminatingModule(new DefaultObjectReceiver<T>() {
			@Override
			public void process(final T object) {
				consumer.accept(object);
			}
		});
	}

	private static class ForwardingStreamPipe
			extends DefaultStreamPipe<StreamReceiver> {

		@Override
		public void startRecord(final String identifier) {
			getReceiver().startRecord(identifier);
		}

		@Override
		public void endRecord() {
			getReceiver().endRecord();
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

	public static class Literal {

		public String name;
		public String value;

		public Literal(final String name, final String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, value);
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof Literal) {
				final Literal other = (Literal) obj;
				return Objects.equals(name, other.name)
						&& Objects.equals(value, other.value);
			}
			return false;
		}
	}

}
