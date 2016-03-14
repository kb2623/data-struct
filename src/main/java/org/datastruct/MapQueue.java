package org.datastruct;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MapQueue<K, V> extends SkipMap<K, V> implements Iterable<V> {

	private int maxSize;
	private int size;

	public MapQueue(int maxCone, int maxSize, Comparator<K> cmp) {
		super(null, cmp);
		super.sentinel = new Entry<>(maxCone);
		this.maxSize = maxSize;
		this.size = 0;
	}

	public MapQueue(int maxCone, int maxSize) {
		this(maxCone, maxSize, (k1, k2) -> k1.hashCode() - k2.hashCode());
	}

	public MapQueue(int maxSize) {
		this(maxSize / 10 < 2 ? 2 : maxSize / 10, maxSize);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		sentinel = new Entry<>(sentinel.conns.length);
		size = 0;
	}

	@Override
	public V put(K k, V v) throws NullPointerException, IllegalArgumentException {
		if (k == null) {
			throw new NullPointerException();
		}
		Entry<K, V> sentinel = (Entry<K, V>) super.sentinel;
		Entry<K, V> e = (Entry<K, V>) getEnrty(k);
		if (e == null) {
			if (size >= maxSize) {
				assert remove(sentinel.next.key) != null : "Bad removel";
			}
			e = new Entry<>(k, v, sentinel.conns.length, sentinel.prev);
			if (sentinel.prev == null) {
				sentinel.next = sentinel.prev = e;
			} else {
				sentinel.prev.prev = e;
				sentinel.prev = e;
			}
			size++;
			return (V) insertEntry(e);
		} else {
			if (sentinel.prev != sentinel.next && sentinel.prev != e) {
				if (e.next == null) {
					sentinel.next = e.prev;
				}
				e.remove();
				e.next = sentinel.prev;
				sentinel.prev.prev = e;
				sentinel.prev = e;
			}
			return (V) e.setValue(v);
		}
	}

	@Override
	public V remove(Object o) throws ClassCastException, NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		K key = (K) o;
		Entry<K, V> sentinel = (Entry<K, V>) super.sentinel;
		Entry<K, V> e = (Entry<K, V>) removeEntry(key);
		if (e == null) {
			return null;
		} else {
			if (sentinel.next != sentinel.prev) {
				if (e.next == null) {
					sentinel.next = sentinel.next.prev;
					sentinel.next.next = null;
				} else if (e.prev == null) {
					sentinel.prev = e.next;
					sentinel.prev.prev = null;
				} else {
					e.remove();
				}
			} else {
				clear();
			}
			size--;
			return (V) e.value;
		}
	}

	@Override
	public V get(Object o) throws ClassCastException, NullPointerException {
		if (o == null) {
			throw new NullPointerException();
		}
		Entry<K, V> sentinel = (Entry<K, V>) super.sentinel;
		K key = (K) o;
		Entry<K, V> e = (Entry<K, V>) getEnrty(key);
		if (e != null) {
			if (sentinel.next != sentinel.prev && sentinel.next == e) {
				sentinel.next = sentinel.next.prev;
			}
			if (e.lowerPriority()) {
				sentinel.prev = e;
			}
			return (V) e.value;
		} else {
			return null;
		}
	}

	@Override
	public Iterator<V> iterator() {
		return new QueueIterator<>();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		for (Entry<K, V> node = ((Entry<K, V>) sentinel).prev; node != null; node = node.next) {
			builder.append(node.key).append('=').append(node.value).append(", ");
		}
		if (builder.length() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append(']');
		return builder.toString();
	}

	class Entry<K, V> extends SkipMap.Entry {

		protected Entry<K, V> prev;
		protected Entry<K, V> next;

		Entry(int maxConns) {
			super(maxConns);
			prev = next = null;
		}

		Entry(K key, V value, int maxCone, Entry<K, V> last) throws NullPointerException {
			super(key, value, maxCone);
			prev = null;
			next = last;
		}

		Entry(Object key, Object value, int maxConns) {
			super(key, value, maxConns);
		}

		boolean lowerPriority() {
			if (prev != null && prev.prev != null) {
				Entry pp = prev.prev, p = prev;
				if (next != null) {
					next.prev = p;
				}
				pp.next = this;
				prev = pp;
				p.prev = this;
				p.next = next;
				next = p;
				return false;
			} else if (prev != null) {
				Entry p = prev;
				if (next != null) {
					next.prev = p;
				}
				p.next = next;
				p.prev = this;
				next = p;
				prev = null;
				return true;
			} else {
				return false;
			}
		}

		void remove() {
			if (prev != null) {
				prev.next = next;
			}
			if (next != null) {
				next.prev = prev;
			}
			prev = next = null;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof Entry)) return false;
			if (this == o) return true;
			Entry<?, ?> that = (Entry<?, ?>) o;
			return super.equals(that);
		}

		@Override
		public int hashCode() {
			int result = super.hashCode();
			result = 31 * result + (prev != null ? prev.hashCode() : 0);
			result = 31 * result + (next != null ? next.hashCode() : 0);
			return result;
		}
	}

	class QueueIterator<V> implements Iterator<V> {

		private Entry<K, V> next;

		QueueIterator() {
			next = ((Entry<K, V>) sentinel).prev;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public V next() {
			if (next == null) {
				throw new NoSuchElementException();
			}
			V ret = (V) next.value;
			next = next.next;
			return ret;
		}
	}
}
