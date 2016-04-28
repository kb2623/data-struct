package org.datastruct.set;

import org.datastruct.Stack;

import java.util.*;

public class SkipSet<E> implements NavigableSet<E> {

	private Node sentinel;
	private CompareKey<E> keyCmp;

	private SkipSet(Node sentinel, Comparator<E> keyCmp) {
		this.sentinel = sentinel;
		this.keyCmp = new CompareKey(keyCmp);
	}

	public SkipSet(int maxCone, Comparator<E> keyCmp) {
		sentinel = new Node(maxCone);
		this.keyCmp = new CompareKey<>(keyCmp);
	}

	public SkipSet(int maxConns) {
		this(maxConns, (e1, e2) -> e1.hashCode() - e2.hashCode());
	}

	@Override
	public E lower(E e) {
		return null;
	}

	@Override
	public E floor(E e) {
		return null;
	}

	@Override
	public E ceiling(E e) {
		return null;
	}

	@Override
	public E higher(E e) {
		return null;
	}

	@Override
	public E pollFirst() {
		return null;
	}

	@Override
	public E pollLast() {
		return null;
	}

	@Override
	public int size() {
		int i = 0;
		for (Node node = sentinel.conns[0]; node != null; node = node.conns[0]) {
			i++;
		}
		return i;
	}

	@Override
	public boolean isEmpty() {
		if (sentinel.conns[0] == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean contains(Object o) {
		if (o == null) throw new NullPointerException();
		E ele = (E) o;
		Node curr = sentinel;
		for (int level = sentinel.conns.length - 1; level >= 0; level--) {
			Node tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = keyCmp.compare(tmp.data, ele);
				if (cmp == 0) {
					return true;
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = keyCmp.compare(tmp.data, ele);
						if (cmp == 0) {
							return true;
						} else if (cmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
		}
		return false;
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
	public boolean add(E ele) {
		Node node = new Node(ele, sentinel.conns.length);
		return insertNode(node) == null;
	}

	protected E insertNode(Node node) {
		Stack<Node> stack = new Stack<>();
		Node curr = sentinel;
		for (int level = sentinel.conns.length - 1; level >= 0; level--) {
			Node tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = keyCmp.compare(tmp.data, node.data);
				if (cmp == 0) {
					return tmp.setData(node.data);
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = keyCmp.compare(tmp.data, node.data);
						if (cmp == 0) {
							return tmp.setData(node.data);
						} else if (cmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
			stack.push(curr);
		}
		for (int level = 0; level < node.conns.length; level++) {
			curr = stack.pop();
			node.conns[level] = curr.conns[level];
			curr.conns[level] = node;
		}
		return null;
	}

	@Override
	public boolean remove(Object o) {
		if (o == null) throw new NullPointerException();
		E ele = (E) o;
		return removeNode(ele) != null;
	}

	private Node removeNode(E ele) {
		Stack<Node> stack = new Stack<>();
		Node curr = sentinel;
		Node found = null;
		for (int level = sentinel.conns.length - 1; level >= 0; level--) {
			Node tmp = curr.conns[level];
			if (tmp != null) {
				int cmp = keyCmp.compare(tmp.data, ele);
				if (cmp == 0) {
					found = tmp;
				} else if (cmp < 0) {
					curr = tmp;
					while (curr.conns[level] != null) {
						tmp = curr.conns[level];
						cmp = keyCmp.compare(tmp.data, ele);
						if (cmp == 0) {
							found = tmp;
							break;
						} else if (cmp > 0) {
							break;
						} else {
							curr = tmp;
						}
					}
				}
			}
			stack.push(curr);
		}
		for (int level = 0; level < sentinel.conns.length && found != null; level++) {
			curr = stack.pop();
			if (found.conns.length > level) {
				curr.conns[level] = found.conns[level];
			} else {
				break;
			}
		}
		return found;
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

	@Override
	public void clear() {
		for (int level = 0; level < sentinel.conns.length; level++) {
			sentinel.conns[level] = null;
		}
	}

	@Override
	public NavigableSet<E> descendingSet() {
		return null;
	}

	@Override
	public Iterator<E> descendingIterator() {
		return null;
	}

	@Override
	public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
		return null;
	}

	@Override
	public NavigableSet<E> headSet(E toElement, boolean inclusive) {
		return null;
	}

	@Override
	public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
		return null;
	}

	@Override
	public Comparator<? super E> comparator() {
		return null;
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		return null;
	}

	@Override
	public SortedSet<E> headSet(E toElement) {
		return null;
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		return null;
	}

	@Override
	public E first() {
		return null;
	}

	@Override
	public E last() {
		return null;
	}

	protected class Node {

		protected Node[] conns;
		protected E data;

		Node(int maxConns) {
			conns = (Node[]) new Object[maxConns];
			data = null;
		}

		Node(E data, int maxConns) throws NullPointerException {
			if (data == null) {
				throw new NullPointerException();
			}
			this.data = data;
			int size;
			for (size = 1; size < maxConns; size++) {
				if ((int) (Math.random() * 2) != 1) {
					break;
				}
			}
			this.conns = (Node[]) new Object[size];
		}

		public E getData() {
			return data;
		}

		public E setData(E data) {
			return this.data;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			if (this == o) return true;
			Node that = (Node) o;
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

	class CompareKey<K> implements Comparator<K> {

		Comparator<K> cmp;

		CompareKey(Comparator<K> cmp) {
			this.cmp = cmp;
		}

		@Override
		public int compare(K k1, K k2) {
			int cmp = this.cmp.compare(k1, k2);
			if (cmp == 0) {
				if (k1.equals(k2)) {
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
