package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MapQueueTest {

	private Map<Integer, Integer> map;
	private List<Integer> listV;
	private List<Integer> listK;

	private int size = 1000;
	private int capacity = 25;

	@Before
	public void setUp() {
		map = new MapQueue<>(5, capacity);
		listV = new LinkedList<>();
		listK = new LinkedList<>();
	}

	@Test
	public void testPut() {
		put(false);
		Iterator<Integer> itM = ((MapQueue<Integer, Integer>) map).iterator();
		Iterator<Integer> itL = listV.iterator();
		while (itL.hasNext()) {
			try {
				assertEquals(itL.next(), itM.next());
			} catch (NoSuchElementException e) {
				fail();
			}
		}
		if (itM.hasNext()) {
			fail();
		}
	}

	private void put(boolean testGet) {
		for (int i = 0; i < size; i++) {
			int rNumK = (int) (Math.random() * (size + 1) + 1);
			int rNumV = (int) (Math.random() * (size + 1000) + 1);
			Integer ret = map.put(rNumK, rNumV);
			if (ret != null) {
				int index = remove(listV, ret);
				listV.add(0, rNumV);
				if (testGet) {
					listK.remove(index);
					listK.add(0, rNumK);
				}
			} else {
				if (listV.size() >= capacity) {
					if (testGet) {
						listK.remove(capacity - 1);
						listK.add(0, rNumK);
					}
					listV.remove(capacity - 1);
					listV.add(0, rNumV);
				} else {
					if (testGet) {
						listK.add(0, rNumK);
					}
					listV.add(0, rNumV);
				}
			}
		}
		if (testGet) {
			assertEquals(listK.size(), map.size());
		}
		assertEquals(listV.size(), map.size());
	}

	private int remove(List<Integer> list, Integer o) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).intValue() - o.intValue() == 0) {
				list.remove(i);
				return i;
			}
		}
		return -1;
	}

	@Test
	public void testGet() {
		put(true);
		for (int i = 0; i < size; i++) {
			int rIndex = (int) (Math.random() * listK.size());
			assertEquals(listV.get(rIndex), map.get(listK.get(rIndex)));
			lowerPririty(rIndex);
			Iterator<Integer> itM = ((MapQueue<Integer, Integer>) map).iterator();
			Iterator<Integer> itL = listV.iterator();
			while (itL.hasNext()) {
				try {
					assertEquals(itL.next(), itM.next());
				} catch (AssertionError e) {
					throw new AssertionError(map.toString() + "\n" + listK.toString(), e);
				}
			}
			if (itM.hasNext()) {
				fail();
			}
		}
	}

	private void lowerPririty(int index) {
		if (index != 0) {
			Integer tmp = listK.set(index - 1, listK.get(index));
			listK.set(index, tmp);
			tmp = listV.set(index - 1, listV.get(index));
			listV.set(index, tmp);
		}
	}

	@Test
	public void testRemove() {
	}
}