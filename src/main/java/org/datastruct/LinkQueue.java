package org.datastruct;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkQueue<E> implements IQueue<E> {

	private Node<E> first;
	private Node<E> last;

	@Override
	public int size() {
		int size = 0;
		Node<E> curr = first;
		while (curr != null) {
			size++;
			curr = curr.prev;
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		return first == null;
	}

	@Override
	public boolean contains(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		E ele = (E) o;
		Node<E> curr = first;
		while (curr != null) {
			if (curr.data.equals(ele)) {
				return true;
			} else {
				curr = curr.prev;
			}
		}
		return false;
	}

	@Override
	public Iterator iterator() {
		return new IterateLinkQueue<>();
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size()];
		Node<E> curr = last;
		for (int i = 0; curr != null; i++) {
			array[i] = curr.data;
			curr = curr.prev;
		}
		return array;
	}

	@Override
	public <T> T[] toArray(T[] ts) throws NullPointerException {
		if (ts == null) {
			throw new NullPointerException();
		}
		int size = size();
		if (ts.length >= size) {
			Node<E> curr = first;
			for (int i = 0; curr != null; i++) {
				ts[i] = (T) curr.data;
				curr = curr.prev;
			}
			return ts;
		} else {
			T[] array = (T[]) new Object[size];
			Node<E> curr = first;
			for (int i = 0; curr != null; i++) {
				array[i] = (T) curr.data;
				curr = curr.prev;
			}
			return array;
		}
	}

	@Override
	public boolean add(E o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		if (isEmpty()) {
			first = last = new Node(o);
		} else {
			Node<E> curr = last;
			last = new Node(o);
			curr.prev = last;
		}
		return true;
	}

	@Override
	public boolean remove(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		E ele = (E) o;
		Node<E> curr = first;
		Node<E> prev = null;
		while (curr != null) {
			if (curr.data.equals(ele)) {
				if (prev == null) {
					first = first.prev;
				} else {
					if (curr == last) {
						last = prev;
						prev.prev = null;
					} else {
						prev.prev = curr.prev;
					}
				}
				if (first == null) {
					last = null;
				}
				return true;
			} else {
				prev = curr;
				curr = curr.prev;
			}
		}
		return false;
	}

	@Override
	public void clear() {
		first = last = null;
	}

	@Override
	public boolean offer(E o) {
		try {
			return add(o);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public E remove() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		if (isEmpty()) {
			return null;
		} else {
			E ret = first.data;
			first = first.prev;
			if (first == null) {
				last = null;
			}
			return ret;
		}
	}

	@Override
	public E poll() {
		try {
			return remove();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public E element() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return first.data;
	}

	@Override
	public E peek() {
		try {
			return element();
		} catch (Exception e) {
			return null;
		}
	}

	class Node<E> {

		E data;
		Node<E> prev;

		Node(E data, Node<E> prev) {
			this.data = data;
			this.prev = prev;
		}

		Node(E data) {
			this(data, null);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Node)) return false;
			Node<?> that = (Node<?>) o;
			return data != null ? data.equals(that.data) : that.data == null;
		}

		@Override
		public int hashCode() {
			int result = data != null ? data.hashCode() : 0;
			result = 31 * result + (prev != null ? prev.hashCode() : 0);
			return result;
		}
	}

	class IterateLinkQueue<E> implements Iterator<E> {

		Node<E> next;

		IterateLinkQueue() {
			next = (Node<E>) first;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public E next() throws NoSuchElementException {
			if (next == null) {
				throw new NoSuchElementException();
			}
			E ret = next.data;
			next = next.prev;
			return ret;
		}
	}
}
