package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class BinomialQueueTest {

	private Queue<Integer> queue;
	private Queue<Integer> tqueue;

	private int size = 1000;

	@Before
	public void setUp() throws Exception {
		CmpInt cmp = new CmpInt();
		queue = new BinomialQueue<>(cmp);
		tqueue = new PriorityQueue<>(cmp);
		assertTrue(queue.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
		queue.clear();
		assertTrue(queue.isEmpty());
	}

	@Test(timeout = 10000)
	public void testContains() throws Exception {
		assertFalse(queue.contains(23));
		add(true);
		while (!tqueue.isEmpty()) {
			Integer i = tqueue.remove();
			try {
				assertTrue(queue.contains(i));
			} catch (AssertionError e) {
				assertTrue(queue.isEmpty());
				throw new AssertionError(queue.toString() + "\ncontains " + i, e);
			}
		}
		try {
			queue.contains(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	@Ignore
	public void testIterator() throws Exception {
		add(true);
		for (Iterator<Integer> it = queue.iterator(); it.hasNext(); ) {
			assertEquals(tqueue.remove(), it.next());
		}
	}

	@Test
	public void testToArray() throws Exception {
		add(true);
		Object[] aq = queue.toArray();
		assertEquals(tqueue.size(), aq.length);
		for (Object o : aq) {
			assertTrue(tqueue.contains(o));

		}
	}

	@Test
	public void testToArray1() throws Exception {
	}

	@Test(timeout = 10000)
	public void testAdd() throws Exception {
		add(true);
		try {
			queue.add(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	private void add(boolean test) {
		for (int i = 0; i < size; i++) {
			int rand = (int) (Math.random() * size * size);
			if (test) {
				assertEquals(tqueue.add(rand), queue.add(rand));
			} else {
				assertTrue(queue.add(rand));
			}
		}
		if (test) {
			assertFalse(queue.isEmpty());
			assertEquals(tqueue.size(), queue.size());
		}
	}

	@Test(timeout = 10000)
	public void testRemove() throws Exception {
		try {
			queue.remove();
			fail();
		} catch (NoSuchElementException e) {
		}
		add(true);
		while (!tqueue.isEmpty()) {
			assertEquals(tqueue.remove(), queue.remove());
		}
		assertEquals(tqueue.size(), queue.size());
		assertEquals(tqueue.isEmpty(), queue.isEmpty());
	}

	@Test(timeout = 10000)
	public void testOffer() throws Exception {
		offer(true);
		assertFalse(queue.offer(null));
	}

	private void offer(boolean test) {
		for (int i = 0; i < size; i++) {
			int rand = (int) (Math.random() * size * size);
			if (test) {
				assertEquals(tqueue.offer(rand), queue.offer(rand));
			} else {
				assertTrue(queue.add(rand));
			}
		}
		if (test) {
			assertFalse(queue.isEmpty());
			assertEquals(tqueue.size(), queue.size());
		}
	}

	@Test(timeout = 10000)
	public void testRemove1() throws Exception {
		add(true);
		for (Object o : queue.toArray()) {
			assertEquals(tqueue.remove(o), queue.remove(o));
			assertEquals(tqueue.size(), queue.size());
		}
		assertTrue(queue.isEmpty());
	}

	@Test(timeout = 10000)
	public void testPoll() throws Exception {
		assertNull(queue.poll());
		offer(true);
		while (!tqueue.isEmpty()) {
			assertEquals(tqueue.poll(), queue.poll());
		}
		assertEquals(tqueue.isEmpty(), queue.isEmpty());
	}

	@Test(timeout = 10000)
	public void testElement() throws Exception {
		try {
			queue.element();
			fail();
		} catch (NoSuchElementException e) {
		}
		add(true);
		while (!tqueue.isEmpty()) {
			assertEquals(tqueue.element(), queue.element());
			assertEquals(tqueue.remove(), queue.remove());
		}
		assertEquals(tqueue.isEmpty(), queue.isEmpty());
	}

	@Test(timeout = 10000)
	public void testPeek() throws Exception {
		assertNull(queue.peek());
		add(true);
		while (!tqueue.isEmpty()) {
			assertEquals(tqueue.peek(), queue.peek());
			assertEquals(tqueue.poll(), queue.poll());
		}
	}

	@Test(timeout = 10000)
	@Ignore
	public void testCopy() {
		add(false);
		BinomialQueue<Integer> cqueue = ((BinomialQueue<Integer>) queue).copy();
		while (!queue.isEmpty()) {
			assertEquals(queue.poll(), cqueue.poll());
		}
		if (!cqueue.isEmpty()) {
			fail();
		}
	}

	private class CmpInt implements Comparator<Integer> {

		@Override
		public int compare(Integer i1, Integer i2) {
			return i1.intValue() - i2.intValue();
		}
	}
}