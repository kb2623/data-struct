package org.datastruct;

import java.util.Collection;
import java.util.Queue;
import java.util.Set;

public interface IQueue<E> extends Queue<E> {

	@Override
	default boolean containsAll(Collection<?> collection) throws NullPointerException, ClassCastException {
		for (Object o : collection) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	default boolean addAll(Collection<? extends E> collection) throws NullPointerException, IllegalArgumentException, IllegalStateException {
		boolean allAdded = true;
		for (E e : collection) {
			if (!add(e)) {
				allAdded = false;
			}
		}
		if (!allAdded) {
			throw new IllegalStateException();
		} else {
			return true;
		}
	}

	@Override
	default boolean removeAll(Collection<?> collection) throws NullPointerException, ClassCastException, UnsupportedOperationException {
		if (collection instanceof Set) {
			for (Object o : collection) {
				while (remove(o)) ;
			}
		} else {
			for (Object o : collection) {
				remove(o);
			}
		}
		return true;
	}

	@Override
	default boolean retainAll(Collection<?> collection) throws NullPointerException, ClassCastException, UnsupportedOperationException {
		Object[] array = toArray();
		for (Object o : array) {
			if (!collection.contains(o)) {
				remove(o);
			}
		}
		return true;
	}
}
