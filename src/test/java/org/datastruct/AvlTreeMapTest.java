package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class AvlTreeMapTest {

	private NavigableMap<Integer, Integer> map;
	private NavigableMap<Integer, Integer> tmap;

	private int size = 1000;

	@Before
	public void setUp() throws Exception {
		Compare cmp = new Compare();
		map = new AvlTreeMap<>(cmp);
		tmap = new TreeMap<>(cmp);
		assertTrue(map.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testPut() throws Exception {
		try {
			map.put(null, 23);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
	}

	private void testPut(boolean test) {
		for (int i = 0; i < size; i++) {
			int rNumK = (int) (Math.random() * (size + 1) + 1);
			int rNumV = (int) (Math.random() * (size + 1000) + 1);
			if (test) {
				assertEquals(tmap.put(rNumK, rNumV), map.put(rNumK, rNumV));
			} else {
				map.put(rNumK, rNumV);
			}
		}
		if (test) {
			assertEquals(tmap.size(), map.size());
			assertFalse(map.isEmpty());
			assertEquals(tmap.toString(), map.toString());
		}
	}

	@Test
	public void testKeySet() throws Exception {
		testKeySetWithIterator();
		testPut(true);
		testKeySetWithIterator();
	}

	private void testKeySetWithIterator() {
		Iterator<Integer> itM = map.keySet().iterator();
		Iterator<Integer> itT = tmap.keySet().iterator();
		while (itM.hasNext() && itT.hasNext()) {
			assertEquals(itT.next(), itM.next());
		}
		if (itM.hasNext() || itT.hasNext()) {
			fail();
		}
	}

	@Test
	public void testValues() throws Exception {
		testValuesWithIterator();
		testPut(true);
		testValuesWithIterator();
	}

	private void testValuesWithIterator() {
		Iterator<Integer> itM = map.values().iterator();
		Iterator<Integer> itT = tmap.values().iterator();
		while (itM.hasNext() && itT.hasNext()) {
			assertEquals(itT.next(), itM.next());
		}
		if (itM.hasNext() || itT.hasNext()) {
			fail();
		}
	}

	@Test
	public void testEntrySet() throws Exception {
		testPut(true);
		Set<Map.Entry<Integer, Integer>> set = map.entrySet();
		Set<Map.Entry<Integer, Integer>> tset = tmap.entrySet();
		assertEquals(tset.size(), set.size());
	}

	@Test
	public void testContainsKey() throws Exception {
		try {
			map.containsKey(null);
			fail();
		} catch (NullPointerException e) {
		}
		int key = (int) (Math.random() * 51 + 100);
		assertFalse(map.containsKey(key));
		testPut(true);
		for (int i = 0; i < size; i++) {
			key = (int) (Math.random() * (size + 1) + 1);
			try {
				assertEquals(tmap.containsKey(key), map.containsKey(key));
			} catch (AssertionError e) {
				throw new AssertionError("Can not find key = " + key + "\n" + tmap.toString() + "\n" + map.toString(), e);
			}
		}
	}

	@Test
	public void testContainsValue() throws Exception {
		int value = (int) (Math.random() * 11 + 100);
		assertFalse(map.containsValue(value));
		testPut(true);
		for (int i = 0; i < size + 200; i++) {
			value = (int) (Math.random() * (size + 2001) + 1);
			assertEquals(tmap.containsValue(value), map.containsValue(value));
		}
	}

	@Test
	public void testGet() throws Exception {
		try {
			map.get(null);
			fail();
		} catch (NullPointerException e) {
		}
		int key = (int) (Math.random() * (size + 1) + 1);
		assertNull(map.get(key));
		testPut(true);
		for (int i = 0; i < size; i++) {
			key = (int) (Math.random() * (size + 1) + 1);
			assertEquals(tmap.get(key), map.get(key));
		}
	}

	@Test
	public void testRemove() throws Exception {
		try {
			map.remove(null);
			fail();
		} catch (NullPointerException e) {
		}
		int key = (int) (Math.random() * 201 + 1);
		assertNull(map.remove(key));
		testPut(true);
		for (int i = 0; i < size; i++) {
			key = (int) (Math.random() * (size + 1) + 1);
			assertEquals(tmap.remove(key), map.remove(key));
			assertEquals(tmap.toString(), map.toString());
		}
		assertEquals(tmap.toString(), map.toString());
	}

	@Test
	public void testPutAll() throws Exception {
		Map<Integer, Integer> nMap = new HashMap<>();
		for (int i = 0; i < 50; i++) {
			int key = (int) (Math.random() * (size + 200 + 1) + 1);
			int value = (int) (Math.random() * 2001 + 1);
			nMap.put(key, value);
		}
		map.putAll(nMap);
		tmap.putAll(nMap);
		assertEquals(tmap.size(), map.size());
		for (Map.Entry<Integer, Integer> e : map.entrySet()) {
			assertEquals(tmap.get(e.getKey()), e.getValue());
		}
	}

	@Test
	public void testSize() throws Exception {
		assertEquals(tmap.size(), map.size());
		testPut(true);
		assertEquals(tmap.size(), map.size());
		map.clear();
		tmap.clear();
		assertEquals(tmap.size(), map.size());
		map.put(23, 23);
	}

	@Test
	public void testMixWith() {
		AvlTreeMap<Integer, Integer> tree = (AvlTreeMap<Integer, Integer>) map;
		assertNull(tree.put(46, 1));
		assertEquals("{46:1}", tree.printTree());
		assertNull(tree.put(19, 2));
		assertEquals("{46:2, 19:1}", tree.printTree());
		assertNull(tree.put(37, 3));
		assertEquals("{37:2, 19:1, 46:1}", tree.printTree());
		assertNull(tree.put(9, 4));
		assertEquals("{37:3, 19:2, 46:1, 9:1}", tree.printTree());
		assertNull(tree.put(20, 5));
		assertEquals("{37:3, 19:2, 46:1, 9:1, 20:1}", tree.printTree());
		assertNull(tree.put(24, 6));
		assertEquals("{20:3, 19:2, 37:2, 9:1, 24:1, 46:1}", tree.printTree());
		assertNull(tree.put(23, 7));
		assertEquals("{20:4, 19:2, 37:3, 9:1, 24:2, 46:1, 23:1}", tree.printTree());
		assertNull(tree.put(41, 8));
		assertEquals("{20:4, 19:2, 37:3, 9:1, 24:2, 46:2, 23:1, 41:1}", tree.printTree());
		assertNull(tree.put(21, 9));
		assertEquals("{20:4, 19:2, 37:3, 9:1, 23:2, 46:2, 21:1, 24:1, 41:1}", tree.printTree());
		assertNull(tree.put(42, 10));
		assertEquals("{20:4, 19:2, 37:3, 9:1, 23:2, 42:2, 21:1, 24:1, 41:1, 46:1}", tree.printTree());
		assertNull(tree.put(13, 11));
		assertEquals("{20:4, 13:2, 37:3, 9:1, 19:1, 23:2, 42:2, 21:1, 24:1, 41:1, 46:1}", tree.printTree());
		assertNull(tree.put(36, 12));
		assertEquals("{23:4, 20:3, 37:3, 13:2, 21:1, 24:2, 42:2, 9:1, 19:1, 36:1, 41:1, 46:1}", tree.printTree());
		assertNull(tree.put(29, 13));
		assertEquals("{23:4, 20:3, 37:3, 13:2, 21:1, 29:2, 42:2, 9:1, 19:1, 24:1, 36:1, 41:1, 46:1}", tree.printTree());
		assertNull(tree.put(7, 14));
		assertEquals("{23:4, 13:3, 37:3, 9:2, 20:2, 29:2, 42:2, 7:1, 19:1, 21:1, 24:1, 36:1, 41:1, 46:1}", tree.printTree());
		assertNull(tree.put(15, 15));
		assertEquals("{23:5, 13:4, 37:3, 9:2, 20:3, 29:2, 42:2, 7:1, 19:2, 21:1, 24:1, 36:1, 41:1, 46:1, 15:1}", tree.printTree());
		assertNull(tree.put(50, 16));
		assertEquals("{23:5, 13:4, 37:4, 9:2, 20:3, 29:2, 42:3, 7:1, 19:2, 21:1, 24:1, 36:1, 41:1, 46:2, 15:1, 50:1}", tree.printTree());
		assertNull(tree.put(44, 17));
		assertEquals("{23:5, 13:4, 37:4, 9:2, 20:3, 29:2, 42:3, 7:1, 19:2, 21:1, 24:1, 36:1, 41:1, 46:2, 15:1, 44:1, 50:1}", tree.printTree());
		assertNull(tree.put(27, 18));
		assertEquals("{23:5, 13:4, 37:4, 9:2, 20:3, 29:3, 42:3, 7:1, 19:2, 21:1, 24:2, 36:1, 41:1, 46:2, 15:1, 27:1, 44:1, 50:1}", tree.printTree());
		assertNull(tree.put(2, 19));
		assertEquals("{23:5, 13:4, 37:4, 7:2, 20:3, 29:3, 42:3, 2:1, 9:1, 19:2, 21:1, 24:2, 36:1, 41:1, 46:2, 15:1, 27:1, 44:1, 50:1}", tree.printTree());
		assertNull(tree.put(32, 20));
		assertEquals("{23:5, 13:4, 37:4, 7:2, 20:3, 29:3, 42:3, 2:1, 9:1, 19:2, 21:1, 24:2, 36:2, 41:1, 46:2, 15:1, 27:1, 32:1, 44:1, 50:1}", tree.printTree());
		assertNull(tree.put(47, 21));
		assertEquals("{23:5, 13:4, 37:4, 7:2, 20:3, 29:3, 46:3, 2:1, 9:1, 19:2, 21:1, 24:2, 36:2, 42:2, 50:2, 15:1, 27:1, 32:1, 41:1, 44:1, 47:1}", tree.printTree());
		assertNull(tree.put(34, 22));
		assertEquals("{23:5, 13:4, 37:4, 7:2, 20:3, 29:3, 46:3, 2:1, 9:1, 19:2, 21:1, 24:2, 34:2, 42:2, 50:2, 15:1, 27:1, 32:1, 36:1, 41:1, 44:1, 47:1}", tree.printTree());
		assertNull(tree.put(35, 23));
		assertEquals("{23:6, 13:4, 37:5, 7:2, 20:3, 29:4, 46:3, 2:1, 9:1, 19:2, 21:1, 24:2, 34:3, 42:2, 50:2, 15:1, 27:1, 32:1, 36:2, 41:1, 44:1, 47:1, 35:1}", tree.printTree());
		assertNull(tree.put(49, 24));
		assertEquals("{23:6, 13:4, 37:5, 7:2, 20:3, 29:4, 46:3, 2:1, 9:1, 19:2, 21:1, 24:2, 34:3, 42:2, 49:2, 15:1, 27:1, 32:1, 36:2, 41:1, 44:1, 47:1, 50:1, 35:1}", tree.printTree());
		assertNull(tree.put(38, 25));
		assertEquals("{23:6, 13:4, 37:5, 7:2, 20:3, 29:4, 46:4, 2:1, 9:1, 19:2, 21:1, 24:2, 34:3, 42:3, 49:2, 15:1, 27:1, 32:1, 36:2, 41:2, 44:1, 47:1, 50:1, 35:1, 38:1}", tree.printTree());
		assertNull(tree.put(5, 26));
		assertEquals("{23:6, 13:4, 37:5, 7:3, 20:3, 29:4, 46:4, 2:2, 9:1, 19:2, 21:1, 24:2, 34:3, 42:3, 49:2, 5:1, 15:1, 27:1, 32:1, 36:2, 41:2, 44:1, 47:1, 50:1, 35:1, 38:1}", tree.printTree());
		assertNull(tree.put(8, 27));
		assertEquals("{23:6, 13:4, 37:5, 7:3, 20:3, 29:4, 46:4, 2:2, 9:2, 19:2, 21:1, 24:2, 34:3, 42:3, 49:2, 5:1, 8:1, 15:1, 27:1, 32:1, 36:2, 41:2, 44:1, 47:1, 50:1, 35:1, 38:1}", tree.printTree());
		assertNull(tree.put(10, 28));
		assertEquals("{23:6, 13:4, 37:5, 7:3, 20:3, 29:4, 46:4, 2:2, 9:2, 19:2, 21:1, 24:2, 34:3, 42:3, 49:2, 5:1, 8:1, 10:1, 15:1, 27:1, 32:1, 36:2, 41:2, 44:1, 47:1, 50:1, 35:1, 38:1}", tree.printTree());
		assertNull(tree.put(43, 29));
		assertEquals("{23:6, 13:4, 37:5, 7:3, 20:3, 29:4, 46:4, 2:2, 9:2, 19:2, 21:1, 24:2, 34:3, 42:3, 49:2, 5:1, 8:1, 10:1, 15:1, 27:1, 32:1, 36:2, 41:2, 44:2, 47:1, 50:1, 35:1, 38:1, 43:1}", tree.printTree());
		assertNull(tree.put(18, 30));
		assertEquals("{23:6, 13:4, 37:5, 7:3, 20:3, 29:4, 46:4, 2:2, 9:2, 18:2, 21:1, 24:2, 34:3, 42:3, 49:2, 5:1, 8:1, 10:1, 15:1, 19:1, 27:1, 32:1, 36:2, 41:2, 44:2, 47:1, 50:1, 35:1, 38:1, 43:1}", tree.printTree());
		assertNull(tree.put(16, 31));
		assertEquals("{23:6, 13:4, 37:5, 7:3, 18:3, 29:4, 46:4, 2:2, 9:2, 15:2, 20:2, 24:2, 34:3, 42:3, 49:2, 5:1, 8:1, 10:1, 16:1, 19:1, 21:1, 27:1, 32:1, 36:2, 41:2, 44:2, 47:1, 50:1, 35:1, 38:1, 43:1}", tree.printTree());
		assertNull(tree.put(17, 32));
		assertEquals("{23:6, 13:4, 37:5, 7:3, 18:3, 29:4, 46:4, 2:2, 9:2, 16:2, 20:2, 24:2, 34:3, 42:3, 49:2, 5:1, 8:1, 10:1, 15:1, 17:1, 19:1, 21:1, 27:1, 32:1, 36:2, 41:2, 44:2, 47:1, 50:1, 35:1, 38:1, 43:1}", tree.printTree());
		assertNull(tree.put(40, 33));
		assertEquals("{23:6, 13:4, 37:5, 7:3, 18:3, 29:4, 46:4, 2:2, 9:2, 16:2, 20:2, 24:2, 34:3, 42:3, 49:2, 5:1, 8:1, 10:1, 15:1, 17:1, 19:1, 21:1, 27:1, 32:1, 36:2, 40:2, 44:2, 47:1, 50:1, 35:1, 38:1, 41:1, 43:1}", tree.printTree());
		assertNull(tree.put(3, 34));
		assertEquals("{23:6, 13:4, 37:5, 7:3, 18:3, 29:4, 46:4, 3:2, 9:2, 16:2, 20:2, 24:2, 34:3, 42:3, 49:2, 2:1, 5:1, 8:1, 10:1, 15:1, 17:1, 19:1, 21:1, 27:1, 32:1, 36:2, 40:2, 44:2, 47:1, 50:1, 35:1, 38:1, 41:1, 43:1}", tree.printTree());
		assertEquals(new Integer(11), tree.remove(13));
		assertEquals("{23:6, 10:4, 37:5, 7:3, 18:3, 29:4, 46:4, 3:2, 9:2, 16:2, 20:2, 24:2, 34:3, 42:3, 49:2, 2:1, 5:1, 8:1, 15:1, 17:1, 19:1, 21:1, 27:1, 32:1, 36:2, 40:2, 44:2, 47:1, 50:1, 35:1, 38:1, 41:1, 43:1}", tree.printTree());
		assertEquals(new Integer(28), tree.remove(10));
		assertEquals("{23:6, 9:4, 37:5, 7:3, 18:3, 29:4, 46:4, 3:2, 8:1, 16:2, 20:2, 24:2, 34:3, 42:3, 49:2, 2:1, 5:1, 15:1, 17:1, 19:1, 21:1, 27:1, 32:1, 36:2, 40:2, 44:2, 47:1, 50:1, 35:1, 38:1, 41:1, 43:1}", tree.printTree());
		assertEquals(new Integer(27), tree.remove(8));
		assertEquals("{23:6, 9:4, 37:5, 3:3, 18:3, 29:4, 46:4, 2:1, 7:2, 16:2, 20:2, 24:2, 34:3, 42:3, 49:2, 5:1, 15:1, 17:1, 19:1, 21:1, 27:1, 32:1, 36:2, 40:2, 44:2, 47:1, 50:1, 35:1, 38:1, 41:1, 43:1}", tree.printTree());
		assertEquals(new Integer(7), tree.remove(23));
		assertEquals("{21:6, 9:4, 37:5, 3:3, 18:3, 29:4, 46:4, 2:1, 7:2, 16:2, 20:2, 24:2, 34:3, 42:3, 49:2, 5:1, 15:1, 17:1, 19:1, 27:1, 32:1, 36:2, 40:2, 44:2, 47:1, 50:1, 35:1, 38:1, 41:1, 43:1}", tree.printTree());
		assertEquals(new Integer(19), tree.remove(2));
		assertEquals("{21:6, 9:4, 37:5, 5:2, 18:3, 29:4, 46:4, 3:1, 7:1, 16:2, 20:2, 24:2, 34:3, 42:3, 49:2, 15:1, 17:1, 19:1, 27:1, 32:1, 36:2, 40:2, 44:2, 47:1, 50:1, 35:1, 38:1, 41:1, 43:1}", tree.printTree());
		assertEquals(new Integer(5), tree.remove(20));
		assertEquals("{21:6, 9:4, 37:5, 5:2, 18:3, 29:4, 46:4, 3:1, 7:1, 16:2, 19:1, 24:2, 34:3, 42:3, 49:2, 15:1, 17:1, 27:1, 32:1, 36:2, 40:2, 44:2, 47:1, 50:1, 35:1, 38:1, 41:1, 43:1}", tree.printTree());
		assertEquals(new Integer(6), tree.remove(24));
		assertEquals("{21:6, 9:4, 37:5, 5:2, 18:3, 34:3, 46:4, 3:1, 7:1, 16:2, 19:1, 29:2, 36:2, 42:3, 49:2, 15:1, 17:1, 27:1, 32:1, 35:1, 40:2, 44:2, 47:1, 50:1, 38:1, 41:1, 43:1}", tree.printTree());
		assertEquals(new Integer(17), tree.remove(44));
		assertEquals("{21:6, 9:4, 37:5, 5:2, 18:3, 34:3, 46:4, 3:1, 7:1, 16:2, 19:1, 29:2, 36:2, 42:3, 49:2, 15:1, 17:1, 27:1, 32:1, 35:1, 40:2, 43:1, 47:1, 50:1, 38:1, 41:1}", tree.printTree());
		assertEquals(new Integer(9), tree.remove(21));
		assertEquals("{19:6, 9:4, 37:5, 5:2, 16:3, 34:3, 46:4, 3:1, 7:1, 15:1, 18:2, 29:2, 36:2, 42:3, 49:2, 17:1, 27:1, 32:1, 35:1, 40:2, 43:1, 47:1, 50:1, 38:1, 41:1}", tree.printTree());
		assertEquals(new Integer(2), tree.remove(19));
		assertEquals("{37:5, 18:4, 46:4, 9:3, 34:3, 42:3, 49:2, 5:2, 16:2, 29:2, 36:2, 40:2, 43:1, 47:1, 50:1, 3:1, 7:1, 15:1, 17:1, 27:1, 32:1, 35:1, 38:1, 41:1}", tree.printTree());
		assertEquals(new Integer(14), tree.remove(7));
		assertEquals("{37:5, 18:4, 46:4, 9:3, 34:3, 42:3, 49:2, 5:2, 16:2, 29:2, 36:2, 40:2, 43:1, 47:1, 50:1, 3:1, 15:1, 17:1, 27:1, 32:1, 35:1, 38:1, 41:1}", tree.printTree());
		assertEquals(new Integer(4), tree.remove(9));
		assertEquals("{37:5, 18:4, 46:4, 5:3, 34:3, 42:3, 49:2, 3:1, 16:2, 29:2, 36:2, 40:2, 43:1, 47:1, 50:1, 15:1, 17:1, 27:1, 32:1, 35:1, 38:1, 41:1}", tree.printTree());
		assertEquals(new Integer(33), tree.remove(40));
		assertEquals("{37:5, 18:4, 46:4, 5:3, 34:3, 42:3, 49:2, 3:1, 16:2, 29:2, 36:2, 38:2, 43:1, 47:1, 50:1, 15:1, 17:1, 27:1, 32:1, 35:1, 41:1}", tree.printTree());
		assertEquals(new Integer(10), tree.remove(42));
		assertEquals("{37:5, 18:4, 46:3, 5:3, 34:3, 41:2, 49:2, 3:1, 16:2, 29:2, 36:2, 38:1, 43:1, 47:1, 50:1, 15:1, 17:1, 27:1, 32:1, 35:1}", tree.printTree());
	}

	@Test
	public void testRemoveRoot() {
		AvlTreeMap<Integer, Integer> tree = (AvlTreeMap<Integer, Integer>) map;
		assertNull(map.put(23, 23));
		assertNull(map.put(32, 32));
		assertEquals("{23:2, 32:1}", tree.printTree());
		assertEquals(new Integer(23), map.remove(23));
		assertEquals("{32:1}", tree.printTree());
		assertEquals(new Integer(32), map.remove(32));
		assertEquals("{}", tree.printTree());
		assertTrue(map.isEmpty());
		assertNull(map.put(32, 32));
		assertNull(map.put(23, 23));
		assertNull(map.put(34, 34));
		assertNull(map.put(33, 33));
		assertNull(map.put(35, 35));
		assertNull(map.put(10, 10));
		assertEquals("{32:3, 23:2, 34:2, 10:1, 33:1, 35:1}", tree.printTree());
		assertEquals(new Integer(32), map.remove(32));
		assertEquals("{23:3, 10:1, 34:2, 33:1, 35:1}", tree.printTree());
		assertEquals(new Integer(23), map.remove(23));
		assertEquals("{34:3, 10:2, 35:1, 33:1}", tree.printTree());
	}

	@Test
	public void testCeilingEntry() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.ceilingEntry(i), map.ceilingEntry(i));
		}
	}

	@Test
	public void testCeilingKey() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.ceilingKey(i), map.ceilingKey(i));
		}
	}

	@Test
	public void testFirstEntry() {
		testPut(true);
		assertEquals(tmap.firstEntry(), map.firstEntry());
	}

	@Test
	public void testLastEntry() {
		testPut(true);
		assertEquals(tmap.lastEntry(), map.lastEntry());
	}

	@Test
	public void testDescendingMap() {
		testPut(true);
		NavigableMap rtmap = tmap.descendingMap();
		NavigableMap rmap = map.descendingMap();
		assertEquals(rtmap.firstEntry(), rmap.firstEntry());
		assertEquals(rtmap.lastEntry(), rmap.lastEntry());
	}

	@Test
	public void testDescendingKeySet() {
		testPut(true);
		NavigableSet<Integer> tset = tmap.descendingKeySet();
		NavigableSet<Integer> mset = map.descendingKeySet();
		Iterator<Integer> tit = tset.iterator();
		Iterator<Integer> mit = mset.iterator();
		while (tit.hasNext()) {
			assertEquals(tit.next(), mit.next());
		}
		if (mit.hasNext()) {
			fail();
		}
	}

	@Test
	public void testFloorEntry() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.floorEntry(i), map.floorEntry(i));
		}
	}

	@Test
	public void testFloorKey() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.floorKey(i), map.floorKey(i));
		}
	}

	@Test
	public void testLowerEntry() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.lowerEntry(i), map.lowerEntry(i));
		}
	}

	@Test
	public void testLowerKey() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.lowerKey(i), map.lowerKey(i));
		}
	}

	@Test
	public void testHigherEntry() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.higherEntry(i), map.higherEntry(i));
		}
	}

	@Test
	public void testHigherKey() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.higherKey(i), map.higherKey(i));
		}
	}

	@Test
	public void testTailMap() {
		try {
			map.tailMap(null, false);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
		int formKey = (int) (Math.random() * size);
		NavigableMap<Integer, Integer> stmap = tmap.tailMap(formKey, true);
		NavigableMap<Integer, Integer> smap = map.tailMap(formKey, true);
		assertEquals(stmap.size(), smap.size());
		for (Map.Entry e : stmap.entrySet()) {
			assertEquals(e.getValue(), smap.get(e.getKey()));
		}
	}

	@Test
	public void testHeadMap() {
		try {
			map.headMap(null, false);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
		int toKey = (int) (Math.random() * size);
		NavigableMap<Integer, Integer> stmap = tmap.headMap(toKey, true);
		NavigableMap<Integer, Integer> smap = map.headMap(toKey, true);
		assertEquals(stmap.size(), smap.size());
		for (Map.Entry e : stmap.entrySet()) {
			assertEquals(e.getValue(), smap.get(e.getKey()));
		}
	}

	@Test
	public void testSubMap() {
		try {
			map.subMap(null, false, 10, false);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			map.subMap(20, false, null, false);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			map.subMap(null, false, null, false);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			map.subMap(200, false, 10, false);
			fail();
		} catch (IllegalArgumentException e) {
		}
		testPut(true);
		int formKey = (int) (Math.random() * size);
		int toKey = formKey + (int) (Math.random() * (size - formKey));
		NavigableMap<Integer, Integer> stmap = tmap.subMap(formKey, true, toKey, true);
		NavigableMap<Integer, Integer> smap = map.subMap(formKey, true, toKey, true);
		assertEquals(stmap.size(), smap.size());
		for (Map.Entry e : stmap.entrySet()) {
			assertEquals(e.getValue(), smap.get(e.getKey()));
		}
	}

	class Compare implements Comparator<Integer> {
		@Override
		public int compare(Integer i1, Integer i2) {
			return i1 - i2;
		}
	}
}