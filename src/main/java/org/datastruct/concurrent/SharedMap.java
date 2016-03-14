package org.datastruct.concurrent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedMap<K, V> implements ConcurrentMap<K, V> {

	private ReadWriteLock lock;
	private Map<K, V> map;

	public SharedMap(Map<K, V> map) {
		this.map = map;
		lock = new ReentrantReadWriteLock();
	}

	@Override
	public int size() {
		lock.readLock().lock();
		try {
			return map.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return map.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsKey(Object o) {
		lock.readLock().lock();
		try {
			return map.containsKey(o);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsValue(Object o) {
		lock.readLock().lock();
		try {
			return map.containsValue(o);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V get(Object o) {
		lock.readLock().lock();
		try {
			return map.get(o);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V put(K key, V value) throws NullPointerException {
		lock.writeLock().lock();
		try {
			return map.put(key, value);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public V remove(Object o) {
		lock.writeLock().lock();
		try {
			return map.remove(o);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) throws NullPointerException {
		for (Entry<? extends K, ? extends V> e : map.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		lock.writeLock().lock();
		try {
			map.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Set<K> keySet() {
		lock.readLock().lock();
		try {
			return map.keySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Collection<V> values() {
		lock.readLock().lock();
		try {
			return map.values();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		lock.readLock().lock();
		try {
			return map.entrySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public String toString() {
		lock.readLock().lock();
		try {
			return super.toString();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V putIfAbsent(K key, V value) {
		if (containsKey(key)) {
			return get(key);
		} else {
			return put(key, value);
		}
	}

	@Override
	public boolean remove(Object key, Object value) {
		if (containsKey(key)) {
			Object o = get(key);
			if (o == null && value == null) {
				remove(key);
				return true;
			} else if (o.equals(value)) {
				remove(key);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		if (containsKey(key)) {
			Object o = get(key);
			if (o == null && oldValue == null) {
				put(key, newValue);
				return true;
			} else if (o.equals(oldValue)) {
				put(key, newValue);
				return true;
			}
		}
		return false;
	}

	@Override
	public V replace(K key, V value) {
		if (containsKey(key)) {
			return put(key, value);
		}
		return null;
	}
}
