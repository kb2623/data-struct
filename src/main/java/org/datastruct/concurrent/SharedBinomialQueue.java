package org.datastruct.concurrent;

import org.datastruct.BinomialQueue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedBinomialQueue<E> extends BinomialQueue<E> {

	private ReadWriteLock lock;

	public SharedBinomialQueue(Comparator<E> cmp) {
		super(cmp);
		lock = new ReentrantReadWriteLock();
	}

	@Override
	public int size() {
		lock.readLock().lock();
		try {
			return super.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return super.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(Object o) {
		lock.readLock().lock();
		try {
			return super.contains(o);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Object[] toArray() {
		lock.readLock().lock();
		try {
			return super.toArray();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public <T> T[] toArray(T[] ts) {
		lock.readLock().lock();
		try {
			return super.toArray(ts);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean add(E e) throws NullPointerException {
		lock.writeLock().lock();
		try {
			return super.add(e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public boolean remove(Object o) {
		lock.writeLock().lock();
		try {
			return super.remove(o);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void clear() {
		lock.writeLock().lock();
		try {
			super.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public boolean offer(E e) {
		lock.writeLock().lock();
		try {
			return super.offer(e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public E remove() {
		lock.writeLock().lock();
		try {
			return super.remove();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public E poll() {
		lock.writeLock().lock();
		try {
			return super.poll();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public E element() {
		lock.readLock().lock();
		try {
			return super.element();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public E peek() {
		lock.readLock().lock();
		try {
			return super.peek();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Iterator<E> iterator() {
		lock.readLock().lock();
		try {
			return super.iterator();
		} finally {
			lock.readLock().unlock();
		}
	}
}
