package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RadixTreeMapTestMap {

	private NavigableMap<String, Double> map;
	private NavigableMap<String, Double> tmap;

	private int size = 1000;

	@Before
	public void setUp() throws Exception {
		map = new RadixTreeMap<>();
		tmap = new TreeMap<>();
	}

	private String getRandomKey(int size) {
		int rNum = (int) (Math.random() * size + 1 + 1);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < rNum; i++) {
			int c = (int) (Math.random() * 26 + 97);
			builder.append((char) c);
		}
		return builder.toString();
	}

	@Test
	public void testSize() throws Exception {
		assertEquals(0, map.size());
		testPut(true);
		assertEquals(tmap.size(), map.size());
	}

	@Test
	public void testIsEmpty() throws Exception {
		assertTrue(map.isEmpty());
		testPut(false);
		assertFalse(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testContainsKey() throws Exception {
		try {
			map.containsKey(null);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
		for (String s : tmap.keySet()) {
			assertTrue(map.containsKey(s));
		}
	}

	@Test
	public void testContainsValue() throws Exception {
		try {
			map.containsValue(null);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
		for (Double d : tmap.values()) {
			assertTrue(map.containsValue(d));
		}
	}

	@Test
	public void testGet() throws Exception {
		try {
			map.get(null);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
		for (String s : tmap.keySet()) {
			assertEquals(tmap.get(s), map.get(s));
		}
	}

	@Test
	public void testPut() throws Exception {
		try {
			map.put(null, 2.34d);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			map.put("asdf", null);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			map.put(null, null);
			fail();
		} catch (NullPointerException e) {
		}
		testPut(true);
	}

	private void testPut(boolean test) {
		for (int i = 0; i < size; i++) {
			double value = Math.random() * 5000 + 1;
			String key = getRandomKey((int) (Math.random() * 26 + 1));
			if (test) {
				assertEquals(tmap.put(key, value), map.put(key, value));
			} else {
				map.put(key, value);
			}
		}
	}

	@Test
	public void testRemove() throws Exception {
		try {
			testPut(true);
			for (String s : tmap.keySet()) {
				if ((int) (Math.random() * 2 + 1) == 0) {
					assertEquals(tmap.remove(s), map.remove(s));
				}
			}
			assertEquals(tmap.size(), map.size());
			Set<Map.Entry<String, Double>> sm = map.entrySet();
			Set<Map.Entry<String, Double>> st = tmap.entrySet();
			assertEquals(st.size(), sm.size());
			for (Map.Entry<String, Double> e : sm) {
				assertEquals(tmap.get(e.getKey()), e.getValue());
			}
		} catch (AssertionError e) {
			((RadixTreeMap) map).printTree();
			throw new AssertionError(e);
		}
	}

	@Test
	public void testPutAll() throws Exception {
		Map<String, Double> addMap = new HashMap<>(50);
		addMap.put("asdf", null);
		try {
			map.putAll(addMap);
			fail();
		} catch (NullPointerException e) {
		}
		addMap.clear();
		addMap.put(null, 23.43);
		try {
			map.putAll(addMap);
			fail();
		} catch (NullPointerException e) {
		}
		addMap.clear();
		for (int i = 0; i < 50; i++) {
			double value = Math.random() * 5000 + 1;
			String key = getRandomKey((int) (Math.random() * 26 + 1));
			addMap.put(key, value);
		}
		map.putAll(addMap);
		tmap.putAll(addMap);
		assertEquals(tmap.size(), map.size());
		for (Map.Entry<String, Double> e : map.entrySet()) {
			assertEquals(tmap.get(e.getKey()), e.getValue());
		}
	}

	@Test
	public void testClear() throws Exception {
		assertTrue(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
		testPut(false);
		assertFalse(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testKeySet() throws Exception {
		try {
			Set<String> sm = map.keySet();
			Set<String> st = tmap.keySet();
			assertEquals(st.size(), sm.size());
			for (String s : sm) {
				assertTrue(tmap.containsKey(s));
			}
			testPut(true);
			sm = map.keySet();
			st = tmap.keySet();
			assertEquals(st.size(), sm.size());
			for (String s : sm) {
				assertTrue(tmap.containsKey(s));
			}
		} catch (AssertionError e) {
			((RadixTreeMap) map).printTree();
			throw new AssertionError(e);
		}
	}

	@Test
	public void testValues() throws Exception {
		try {
			Collection<Double> sm = map.values();
			Collection<Double> st = tmap.values();
			assertEquals(st.size(), sm.size());
			for (Double d : sm) {
				assertTrue(tmap.containsValue(d));
			}
			testPut(true);
			sm = map.values();
			st = tmap.values();
			assertEquals(st.size(), sm.size());
			for (Double d : sm) {
				assertTrue(tmap.containsValue(d));
			}
		} catch (AssertionError e) {
			((RadixTreeMap) map).printTree();
			throw new AssertionError(e);
		}
	}

	@Test
	public void testEntrySet() throws Exception {
		try {
			Set<Map.Entry<String, Double>> sm = map.entrySet();
			Set<Map.Entry<String, Double>> st = tmap.entrySet();
			assertEquals(st.size(), sm.size());
			for (Map.Entry<String, Double> e : sm) {
				assertEquals(tmap.get(e.getKey()), e.getValue());
			}
			testPut(true);
			sm = map.entrySet();
			st = tmap.entrySet();
			assertEquals(st.size(), sm.size());
			for (Map.Entry<String, Double> e : sm) {
				assertEquals(tmap.get(e.getKey()), e.getValue());
			}
		} catch (AssertionError e) {
			((RadixTreeMap) map).printTree();
			throw new AssertionError(e);
		}
	}

	/*
	@Test
	@Ignore
	public void testCeilingEntry() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.ceilingEntry(i), map.ceilingEntry(i));
		}
	}

	@Test
	@Ignore
	public void testCeilingKey() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.ceilingKey(i), map.ceilingKey(i));
		}
	}

	@Test
	@Ignore
	public void testFirstEntry() {
		testPut(true);
		assertEquals(tmap.firstEntry(), map.firstEntry());
	}

	@Test
	@Ignore
	public void testLastEntry() {
		testPut(true);
		assertEquals(tmap.lastEntry(), map.lastEntry());
	}

	@Test
	@Ignore
	public void testDescendingMap() {
		testPut(true);
		NavigableMap rtmap = tmap.descendingMap();
		NavigableMap rmap = map.descendingMap();
		assertEquals(rtmap.firstEntry(), rmap.firstEntry());
		assertEquals(rtmap.lastEntry(), rmap.lastEntry());
	}

	@Test
	@Ignore
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
	@Ignore
	public void testFloorEntry() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.floorEntry(i), map.floorEntry(i));
		}
	}

	@Test
	@Ignore
	public void testFloorKey() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.floorKey(i), map.floorKey(i));
		}
	}

	@Test
	@Ignore
	public void testLowerEntry() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.lowerEntry(i), map.lowerEntry(i));
		}
	}

	@Test
	@Ignore
	public void testLowerKey() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.lowerKey(i), map.lowerKey(i));
		}
	}

	@Test
	@Ignore
	public void testHigherEntry() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.higherEntry(i), map.higherEntry(i));
		}
	}

	@Test
	@Ignore
	public void testHigherKey() {
		testPut(true);
		for (int i = 0; i < size; i++) {
			assertEquals(tmap.higherKey(i), map.higherKey(i));
		}
	}

	@Test
	@Ignore
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
	@Ignore
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
	@Ignore
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
	*/
}