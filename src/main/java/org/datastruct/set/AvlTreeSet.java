package org.datastruct.set;

import org.datastruct.LinkQueue;

import java.util.*;

public class AvlTreeSet<E> implements NavigableSet<E> {

	private AvlNode root = null;
	private CompareKey<E> keyCmp;

	public AvlTreeSet() {
		this((k1, k2) -> k1.hashCode() - k2.hashCode());
	}

	public AvlTreeSet(Comparator<E> keyCmp) {
		this.keyCmp = new CompareKey<>(keyCmp);
	}

	@Override
	public int size() {
		if (!isEmpty()) {
			int size = 0;
			Stack<AvlNode> stack = new Stack<>();
			AvlNode curr = root;
			while (!stack.isEmpty() || curr != null) {
				if (curr != null) {
					size++;
					if (curr.higher != null) {
						stack.push(curr.higher);
					}
					curr = curr.lower;
				} else {
					curr = stack.pop();
				}
			}
			return size;
		} else {
			return 0;
		}
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public boolean contains(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return false;
		} else {
			E data = (E) o;
			AvlNode curr = root;
			while (true) {
				int cmp = keyCmp.compare(data, curr.data);
				if (cmp == 0) {
					return true;
				} else if (cmp > 0) {
					if (curr.higher != null) {
						curr = curr.higher;
					} else {
						return false;
					}
				} else {
					if (curr.lower != null) {
						curr = curr.lower;
					} else {
						return false;
					}
				}
			}
		}
	}

	private void updateTree(Stack<AvlNode> stack) {
		int cmp;
		AvlNode curr;
		while (!stack.isEmpty()) {
			curr = stack.pop();
			cmp = curr.getBalance();
			if (cmp < -1 || cmp > 1) {
				if (stack.isEmpty()) {
					root = rotate(curr, cmp);
				} else if (stack.peek().lower == curr) {
					stack.peek().lower = rotate(curr, cmp);
				} else {
					stack.peek().higher = rotate(curr, cmp);
				}
				return;
			}
		}
	}

	private AvlNode rotate(AvlNode node, int cmp) {
		if (cmp < 0) {
			AvlNode cNode = node.higher;
			if (cNode.getBalance() > 0) {
				node.higher = rotateRight(cNode);
			}
			return rotateLeft(node);
		} else {
			AvlNode cNode = node.lower;
			if (cNode.getBalance() < 0) {
				node.lower = rotateLeft(cNode);
			}
			return rotateRight(node);
		}
	}

	private AvlNode rotateLeft(AvlNode node) {
		AvlNode nRoot = node.higher;
		node.higher = nRoot.lower;
		nRoot.lower = node;
		return nRoot;
	}

	private AvlNode rotateRight(AvlNode node) {
		AvlNode nRoot = node.lower;
		node.lower = nRoot.higher;
		nRoot.higher = node;
		return nRoot;
	}

	@Override
	public boolean remove(Object o) throws NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return false;
		} else {
			E data = (E) o;
			Stack<AvlNode> stack = new Stack<>();
			AvlNode found = findNode(data, stack);
			if (found != null) {
				deleteFoundNode(found, stack);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	private AvlNode findNode(final E data, final Stack<AvlNode> stack) {
		AvlNode found = root;
		while (true) {
			stack.push(found);
			int cmp = keyCmp.compare(data, found.data);
			if (cmp == 0) {
				break;
			} else if (cmp > 0) {
				if (found.higher != null) {
					found = found.higher;
				} else {
					return null;
				}
			} else {
				if (found.lower != null) {
					found = found.lower;
				} else {
					return null;
				}
			}
		}
		return found;
	}

	private AvlNode deleteFoundNode(final AvlNode node, final Stack<AvlNode> stack) {
		AvlNode retNode = node;
		AvlNode minNode = node;
		if (node.lower != null) {
			minNode = node.lower;
			if (minNode.higher != null) {
				while (minNode.higher != null) {
					stack.push(minNode);
					minNode = minNode.higher;
				}
				stack.peek().higher = minNode.lower;
				retNode = replaceValues(node, minNode);
			} else {
				if (stack.pop() == root) {
					stack.push(node);
					node.lower = minNode.lower;
					retNode = replaceValues(node, minNode);
				} else {
					if (stack.peek().lower == node) {
						stack.peek().lower = minNode;
					} else {
						stack.peek().higher = minNode;
					}
					minNode.higher = node.higher;
					stack.push(minNode);
				}
			}
		} else if (node.higher != null) {
			minNode = minNode.higher;
			node.lower = minNode.lower;
			node.higher = minNode.higher;
			retNode = replaceValues(node, minNode);
		} else {
			if (node == root) {
				root = null;
			} else {
				stack.pop();
				if (stack.peek().lower == node) {
					stack.peek().lower = null;
				} else {
					stack.peek().higher = null;
				}
			}
		}
		updateTree(stack);
		return retNode;
	}

	private AvlNode replaceValues(AvlNode n1, AvlNode n2) {
		E data = n1.data;
		n1.setData(data);
		n2.setData(data);
		return n2;
	}

	@Override
	public void clear() {
		root = null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		Stack<AvlNode> stack = new Stack<>();
		AvlNode curr = root;
		while (!stack.isEmpty() || curr != null) {
			if (curr != null) {
				stack.push(curr);
				curr = curr.lower;
			} else {
				curr = stack.pop();
				builder.append(curr.data);
				builder.append(", ");
				curr = curr.higher;
			}
		}
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append('}');
		return builder.toString();
	}

	@Deprecated
	public String printTree() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		LinkQueue<AvlNode> queue = new LinkQueue<>();
		AvlNode curr = root;
		while (!queue.isEmpty() || curr != null) {
			builder.append(curr.data).append(':').append(curr.getHeight()).append(", ");
			if (curr.lower != null) {
				queue.offer(curr.lower);
			}
			if (curr.higher != null) {
				queue.offer(curr.higher);
			}
			curr = queue.poll();
		}
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append('}');
		return builder.toString();
	}

	@Override
	public Comparator<? super E> comparator() {
		return keyCmp;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO
		return null;
	}

	@Override
	public E lower(E data) {
		if (data == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		}
		Stack<AvlNode> stack = new Stack<>();
		findNode(data, stack);
		AvlNode e = stack.pop();
		if (e.lower != null && keyCmp.compare(data, e.data) <= 0) {
			e = e.lower;
			while (e.higher != null) {
				e = e.higher;
			}
			return e.data;
		}
		for (; e != null; e = stack.pop()) {
			if (keyCmp.compare(data, e.data) > 0) {
				return e.data;
			}
		}
		return null;
	}

	@Override
	public E floor(E data) {
		if (data == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		}
		Stack<AvlNode> stack = new Stack<>();
		AvlNode e = findNode(data, stack);
		if (e != null) {
			return e.data;
		}
		for (e = stack.pop(); e != null; e = stack.pop()) {
			if (keyCmp.compare(data, e.data) > 0) {
				return e.data;
			}
		}
		return null;
	}

	@Override
	public E ceiling(E data) {
		if (data == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		}
		Stack<AvlNode> stack = new Stack<>();
		AvlNode curr = findNode(data, stack);
		if (curr != null) {
			return curr.data;
		}
		curr = stack.pop();
		if (keyCmp.compare(data, curr.data) < 0) {
			return curr.data;
		} else if (stack.peek().lower == curr) {
			return stack.peek().data;
		} else if (keyCmp.compare(data, curr.data) < 0) {
			return curr.data;
		}
		for (curr = stack.pop(); curr != null; curr = stack.pop()) {
			if (keyCmp.compare(data, curr.data) < 0) {
				return curr.data;
			}
		}
		return null;
	}

	@Override
	public E higher(E data) {
		if (data == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			return null;
		}
		Stack<AvlNode> stack = new Stack<>();
		findNode(data, stack);
		AvlNode e = stack.pop();
		if (keyCmp.compare(data, e.data) < 0) {
			return e.data;
		} else if (e.higher != null) {
			e = e.higher;
			while (e.lower != null) {
				e = e.lower;
			}
			return e.data;
		}
		for (; e != null; e = stack.pop()) {
			if (keyCmp.compare(data, e.data) < 0) {
				return e.data;
			}
		}
		return null;
	}

	@Override
	public E pollFirst() {
		if (isEmpty()) {
			return null;
		} else {
			Stack<AvlNode> stack = new Stack<>();
			AvlNode found = root;
			while (true) {
				stack.push(found);
				if (found.lower != null) {
					found = found.lower;
				} else {
					break;
				}
			}
			return deleteFoundNode(found, stack).data;
		}
	}

	@Override
	public E pollLast() {
		if (isEmpty()) {
			return null;
		} else {
			Stack<AvlNode> stack = new Stack<>();
			AvlNode found = root;
			while (true) {
				stack.push(found);
				if (found.higher != null) {
					found = found.higher;
				} else {
					break;
				}
			}
			return deleteFoundNode(found, stack).data;
		}
	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return null;
	}

	@Override
	public boolean add(E e) {
		if (e == null) {
			throw new NullPointerException();
		} else if (isEmpty()) {
			root = new AvlNode(e);
			return true;
		}
		Stack<AvlNode> stack = new Stack<>();
		AvlNode curr = root;
		int cmp;
		while (true) {
			cmp = keyCmp.compare(e, curr.data);
			stack.push(curr);
			if (cmp == 0) {
				return false;
			} else if (cmp > 0) {
				if (curr.higher != null) {
					curr = curr.higher;
				} else {
					break;
				}
			} else {
				if (curr.lower != null) {
					curr = curr.lower;
				} else {
					break;
				}
			}
		}
		if (cmp > 0) {
			curr.higher = new AvlNode(e);
		} else {
			curr.lower = new AvlNode(e);
		}
		updateTree(stack);
		return true;
	}

	@Override
	public NavigableSet<E> descendingSet() {
		NavigableSet<E> nset = new AvlTreeSet<>(keyCmp.reversed());
		// TODO sprehodi se cez elemente ter jih dodaj na nset
		return nset;
	}

	@Override
	public Iterator<E> descendingIterator() {
		// TODO: 4/27/16  
		return null;
	}

	@Override
	public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
		// TODO: 4/27/16  
		return null;
	}

	@Override
	public NavigableSet<E> headSet(E toElement, boolean inclusive) {
		// TODO: 4/27/16  
		return null;
	}

	@Override
	public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
		// TODO: 4/27/16  
		return null;
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		// TODO: 4/27/16  
		return null;
	}

	@Override
	public SortedSet<E> headSet(E toElement) {
		// TODO: 4/27/16  
		return null;
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		// TODO: 4/27/16  
		return null;
	}

	@Override
	public E first() {
		if (!isEmpty()) {
			AvlNode curr = root;
			while (true) {
				if (curr.lower != null) {
					curr = curr.lower;
				} else {
					return curr.data;
				}
			}
		}
		return null;
	}

	@Override
	public E last() {
		if (!isEmpty()) {
			AvlNode curr = root;
			while (true) {
				if (curr.higher != null) {
					curr = curr.higher;
				} else {
					return curr.data;
				}
			}
		}
		return null;
	}

	private class CompareKey<E> implements Comparator<E> {

		private Comparator<E> cmp;

		private CompareKey(Comparator<E> cmp) {
			this.cmp = cmp;
		}

		private CompareKey() {
			this((k1, k2) -> k1.hashCode() - k2.hashCode());
		}

		@Override
		public int compare(E k1, E k2) {
			int res = this.cmp.compare(k1, k2);
			if (res == 0) {
				if (k1.equals(k2)) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return res;
			}
		}
	}

	private class AvlNode {

		E data;
		AvlNode lower;
		AvlNode higher;

		private AvlNode(E data, AvlNode lower, AvlNode higher) {
			this.data = data;
			this.lower = lower;
			this.higher = higher;
		}

		private AvlNode(E data) {
			this(data, null, null);
		}

		private int getHeight() {
			int height_lower = 1;
			int height_higher = 1;
			if (lower != null) {
				height_lower += lower.getHeight();
			}
			if (higher != null) {
				height_higher += higher.getHeight();
			}
			if (height_lower >= height_higher) {
				return height_lower;
			} else {
				return height_higher;
			}
		}

		private int getBalance() {
			return (lower != null ? lower.getHeight() : 0) - (higher != null ? higher.getHeight() : 0);
		}

		private E getData() {
			return data;
		}

		private E setData(E data) {
			E tmp = this.data;
			this.data = data;
			return tmp;
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			AvlNode lower = this.lower != null ? (AvlNode) this.lower.clone() : null;
			AvlNode higher = this.higher != null ? (AvlNode) this.higher.clone() : null;
			return new AvlNode(data, lower, higher);
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			if (this == o) return true;
			AvlNode that = (AvlNode) o;
			return getData() != null ? getData().equals(that.getData()) : that.getData() == null;
		}

		@Override
		public int hashCode() {
			return getData() != null ? getData().hashCode() : 0;
		}

		@Override
		public String toString() {
			return data.toString();
		}
	}
}
