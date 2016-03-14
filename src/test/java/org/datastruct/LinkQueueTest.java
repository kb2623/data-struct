package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import static org.junit.Assert.*;

public class LinkQueueTest {

	private Queue<Integer> queue;
	private Queue<Integer> tqueue;

	private int size = 1000;

	@Before
	public void setUp() throws Exception {
		queue = new LinkQueue<>();
		tqueue = new LinkedList<>();
		assertTrue(queue.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
		queue.clear();
		assertTrue(queue.isEmpty());
	}

	@Test
	public void testAdd() throws Exception {
		try {
			queue.add(null);
			fail();
		} catch (NullPointerException e) {
		}
		add(true);
		Iterator<Integer> itQ = queue.iterator();
		Iterator<Integer> itT = tqueue.iterator();
		while (itT.hasNext()) {
			assertEquals(itT.next(), itQ.next());
		}
		if (itQ.hasNext()) {
			fail();
		}
		try {
			itQ.next();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	private void add(boolean test) {
		for (int i = 0; i < size; i++) {
			int num = (int) (Math.random() * (size * size));
			if (test) {
				assertEquals(tqueue.add(num), queue.add(num));
			} else {
				assertTrue(queue.add(num));
			}
		}
		assertFalse(queue.isEmpty());
		if (test) {
			assertEquals(tqueue.size(), queue.size());
		}
	}

	@Test
	public void testRemove() throws Exception {
		add(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tqueue.remove(), queue.remove());
		}
		assertTrue(queue.isEmpty());
		try {
			queue.remove();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testContainsAll() throws Exception {
		add(true);
		assertTrue(queue.containsAll(tqueue));
	}

	@Test
	public void testOffer() throws Exception {
		assertFalse(queue.offer(null));
		for (int i = 0; i < size; i++) {
			int num = (int) (Math.random() * (size * size));
			assertEquals(tqueue.offer(num), queue.offer(num));
		}
		assertFalse(queue.isEmpty());
		Iterator<Integer> itQ = queue.iterator();
		Iterator<Integer> itT = tqueue.iterator();
		while (itT.hasNext()) {
			assertEquals(itT.next(), itQ.next());
		}
		if (itQ.hasNext()) {
			fail();
		}
	}

	@Test
	public void testPoll() throws Exception {
		assertNull(queue.poll());
		add(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tqueue.poll(), queue.poll());
		}
		assertNull(queue.poll());
	}

	@Test
	public void testElement() throws Exception {
		try {
			queue.element();
			fail();
		} catch (NoSuchElementException e) {
		}
		add(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tqueue.element(), queue.element());
			queue.remove();
			tqueue.remove();
		}
		try {
			queue.element();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testPeek() throws Exception {
		assertNull(queue.peek());
		add(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tqueue.peek(), queue.peek());
			queue.remove();
			tqueue.remove();
		}
		assertNull(queue.peek());
	}
}