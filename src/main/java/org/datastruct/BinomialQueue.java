package org.datastruct;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinomialQueue<E> implements IQueue<E> {

	private Node<E> root;
	private NodeComparator cmp;

	public BinomialQueue(Comparator<E> cmp) {
		this.cmp = new NodeComparator(cmp);
		root = null;
	}

	public BinomialQueue() {
		this((e1, e2) -> e1.hashCode() - e2.hashCode());
	}

	@Override
	public int size() {
		int size = 0;
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		while (!stack.isEmpty() || curr != null) {
			if (curr == null) {
				curr = stack.pop();
				size++;
				curr = curr.sibling;
			} else if (curr.chield != null) {
				stack.push(curr);
				curr = curr.chield;
			} else {
				size++;
				curr = curr.sibling;
			}
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public boolean contains(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		E e = (E) o;
		Stack<Node> stack = new Stack<>();
		Node<E> curr = root;
		while (curr != null || !stack.isEmpty()) {
			if (curr == null) {
				curr = stack.pop();
			} else {
				int cmp = this.cmp.compare(curr.data, e);
				if (cmp == 0) {
					break;
				} else if (cmp < 0) {
					if (curr.sibling != null) {
						stack.push(curr.sibling);
					}
					curr = curr.chield;
				} else {
					curr = curr.sibling;
				}
			}
		}
		return curr != null;
	}

	@Override
	public Iterator<E> iterator() {
		try {
			return new IteratorBQ<E>((BinomialQueue<E>) clone());
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size()];
		return fillArray(array);
	}

	private <T> T[] fillArray(T[] array) {
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		int i = 0;
		while (!stack.isEmpty() || curr != null) {
			if (curr == null) {
				curr = stack.pop();
				array[i] = (T) curr.data;
				i++;
				curr = curr.sibling;
			} else if (curr.chield != null) {
				stack.push(curr);
				curr = curr.chield;
			} else {
				array[i] = (T) curr.data;
				i++;
				curr = curr.sibling;
			}
		}
		return array;
	}

	@Override
	public <T> T[] toArray(T[] ts) throws NullPointerException {
		if (ts == null) {
			throw new NullPointerException();
		} else if (ts.length >= size()) {
			return fillArray(ts);
		} else {
			return (T[]) toArray();
		}
	}

	@Override
	public boolean add(E e) throws NullPointerException {
		if (e == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			root = new Node(e, null, null, null);
		} else {
			root = new Node(e, null, root, null);
			while (root.sibling != null && root.depth == root.sibling.depth) {
				root = merge(root, root.sibling);
			}
		}
		return true;
	}

	private Node merge(Node<E> prev, Node<E> next) {
		if (cmp.compare(next.data, prev.data) < 0) {
			prev.sibling = next.chield;
			next.chield = prev;
			next.parent = prev.parent;
			prev.parent = next;
			next.updateDepth();
			return next;
		} else {
			prev.sibling = next.sibling;
			next.sibling = prev.chield;
			prev.chield = next;
			next.parent = prev;
			prev.updateDepth();
			return prev;
		}
	}

	@Override
	public boolean remove(Object o) throws NullPointerException, ClassCastException {
		if (o == null) {
			throw new NullPointerException();
		}
		E e = (E) o;
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		while (curr != null || !stack.isEmpty()) {
			if (curr == null) {
				curr = stack.pop();
			} else {
				int cmp = this.cmp.compare((E) curr.data, e);
				if (cmp == 0) {
					break;
				} else if (cmp < 0) {
					if (curr.sibling != null) {
						stack.push(curr.sibling);
					}
					curr = curr.chield;
				} else {
					curr = curr.sibling;
				}
			}
		}
		if (curr != null) {
			removeNode(curr);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void clear() {
		root = null;
	}

	@Override
	public boolean offer(E e) {
		if (e == null) {
			return false;
		} else {
			return add(e);
		}
	}

	private E removeFirst() {
		if (isEmpty()) {
			return null;
		} else {
			Node min = getFirst();
			removeNode(min);
			return (E) min.data;
		}
	}

	private void removeNode(final Node node) {
		Node curr = node, prev;
		Stack<Node> stack = new Stack<>();
		while (curr.parent != null) {
			E pVal = (E) curr.parent.data;
			curr.parent.data = curr.data;
			curr.data = pVal;
			curr = curr.parent;
		}
		if (curr == root) {
			root = root.sibling;
		} else {
			prev = root;
			while (prev.sibling != null) {
				if (prev.sibling == curr) {
					break;
				} else {
					prev = prev.sibling;
				}
			}
			prev.sibling = curr.sibling;
		}
		curr = curr.chield;
		while (curr != null) {
			curr.parent = null;
			stack.push(curr);
			curr = curr.sibling;
		}
		curr = root;
		prev = null;
		while (curr != null && !stack.isEmpty()) {
			if (curr.depth == stack.peek().depth) {
				if (prev != null) {
					prev.sibling = merge(stack.pop(), curr);
					curr = prev.sibling.sibling;
					prev = prev.sibling;
				} else {
					root = this.merge(stack.pop(), root);
					prev = root;
					curr = root.sibling;
				}
			} else {
				if (prev != null) {
					prev.sibling = stack.pop();
					prev.sibling.sibling = curr;
					prev = prev.sibling;
				} else {
					root = stack.pop();
					root.sibling = curr;
					prev = root;
				}
			}
		}
		while (!stack.isEmpty()) {
			if (prev != null) {
				prev.sibling = stack.pop();
				prev.sibling.sibling = null;
				prev = prev.sibling;
			} else {
				root = stack.pop();
				root.sibling = null;
				prev = root;
			}
		}
		if (root != null) {
			fixAll();
		}
	}

	private void fixAll() {
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		while (curr.sibling != null) {
			stack.push(curr);
			curr = curr.sibling;
		}
		while (!stack.isEmpty()) {
			curr = stack.pop();
			if (curr.depth == curr.sibling.depth) {
				while (curr.sibling != null && curr.depth == curr.sibling.depth) {
					curr = merge(curr, curr.sibling);
				}
				if (!stack.isEmpty()) {
					stack.peek().sibling = curr;
				} else {
					root = curr;
					curr = null;
				}
			}
		}
	}

	@Override
	public E remove() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return removeFirst();
	}

	@Override
	public E poll() {
		return removeFirst();
	}

	private Node<E> getFirst() {
		if (isEmpty()) {
			return null;
		} else {
			Node<E> min = root;
			Node<E> curr = root.sibling;
			while (curr != null) {
				if (cmp.compare(min.data, curr.data) > 0) {
					min = curr;
				}
				curr = curr.sibling;
			}
			return min;
		}
	}

	@Override
	public E element() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException();
		} else {
			return getFirst().data;
		}
	}

	@Override
	public E peek() {
		if (isEmpty()) {
			return null;
		} else {
			return getFirst().data;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		Stack<Node> stack = new Stack<>();
		Node curr = root;
		while (curr != null || !stack.isEmpty()) {
			if (curr == null) {
				curr = stack.pop();
				builder.append(curr.data).append(", ");
				curr = curr.sibling;
			} else if (curr.chield != null) {
				stack.push(curr);
				curr = curr.chield;
			} else {
				builder.append(curr.data).append(", ");
				curr = curr.sibling;
			}
		}
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append(']');
		return builder.toString();
	}

	public BinomialQueue<E> copy() {
		try {
			return (BinomialQueue<E>) clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		BinomialQueue<E> clone = new BinomialQueue<>(cmp.cmp);
		clone.root = root != null ? (Node<E>) root.clone() : null;
		return clone;
	}

	class IteratorBQ<E> implements Iterator<E> {

		private BinomialQueue<E> queue;

		IteratorBQ(BinomialQueue<E> queue) throws CloneNotSupportedException {
			this.queue = (BinomialQueue<E>) queue.clone();
		}

		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}

		@Override
		public E next() {
			return (E) queue.remove();
		}
	}

	private class Node<E> {

		E data;
		Node<E> parent;
		Node<E> sibling;
		Node<E> chield;
		int depth;

		Node(E data, Node parent, Node sibling, Node chield, int depth) {
			this.data = data;
			this.parent = parent;
			this.sibling = sibling;
			this.chield = chield;
			this.depth = depth;
		}

		Node(E data, Node parnt, Node sibling, Node chield) {
			this(data, parnt, sibling, chield, 0);
			updateDepth();
		}

		void updateDepth() {
			if (chield != null) {
				depth = chield.depth + 1;
			} else {
				depth = 1;
			}
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			Node<E> parent = this.parent != null ? (Node<E>) this.parent.clone() : null;
			Node<E> sibling = this.sibling != null ? (Node<E>) this.sibling.clone() : null;
			Node<E> chield = this.chield != null ? (Node<E>) this.chield.clone() : null;
			return new Node(this.data, parent, sibling, chield, this.depth);
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof Node)) return false;
			if (this == o) return true;
			Node<?> that = (Node<?>) o;
			return data != null ? data.equals(that.data) : that.data == null;
		}

		@Override
		public int hashCode() {
			int result = data != null ? data.hashCode() : 0;
			result = 31 * result + (parent != null ? parent.hashCode() : 0);
			result = 31 * result + (sibling != null ? sibling.hashCode() : 0);
			result = 31 * result + (chield != null ? chield.hashCode() : 0);
			result = 31 * result + depth;
			return result;
		}
	}

	private class NodeComparator implements Comparator<E> {

		private Comparator<E> cmp;

		NodeComparator(Comparator<E> cmp) {
			this.cmp = cmp;
		}

		@Override
		public int compare(E n1, E n2) {
			int cmp = this.cmp.compare(n1, n2);
			if (cmp == 0) {
				if (n1.equals(n2)) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return cmp;
			}
		}
	}
}
