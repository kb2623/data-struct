package org.datastruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SkipMapTest {

	private NavigableMap<Integer, Integer> map;
	private NavigableMap<Integer, Integer> tmap;

	private int size = 1000;
	private int conns = 6;

	@Before
	public void start() throws Exception {
		Compare cmp = new Compare();
		map = new SkipMap<>(conns, cmp);
		tmap = new TreeMap<>(cmp);
		assertTrue(map.isEmpty());
	}

	@After
	public void end() {
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
		testEntrySetWithIterator();
		testPut(true);
		testEntrySetWithIterator();
	}

	private void testEntrySetWithIterator() {
		Iterator<Map.Entry<Integer, Integer>> itM = map.entrySet().iterator();
		Iterator<Map.Entry<Integer, Integer>> itT = tmap.entrySet().iterator();
		while (itM.hasNext() && itT.hasNext()) {
			Map.Entry<Integer, Integer> eM = itM.next();
			Map.Entry<Integer, Integer> eT = itT.next();
			assertEquals(eT.getKey(), eM.getKey());
			assertEquals(eT.getValue(), eM.getValue());
		}
		if (itM.hasNext() || itT.hasNext()) {
			fail();
		}
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
			try {
				assertEquals(tmap.remove(key), map.remove(key));
			} catch (AssertionError e) {
				String s = "Error at ramoveing key = " + key + "\n" +
						tmap.toString() + "\n" +
						map.toString();
				throw new AssertionError(s, e);
			}
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
		assertEquals(tmap.toString(), map.toString());
	}

	@Test
	public void testSize() throws Exception {
		assertEquals(tmap.size(), map.size());
		testPut(true);
		assertEquals(tmap.size(), map.size());
		map.clear();
		tmap.clear();
		assertEquals(tmap.size(), map.size());
	}

	@Test
	public void testPrintTtree() {
		testPut(false);
		System.out.println(((SkipMap) map).printTree());
		System.out.println(map.toString());
		testPut(false);
		System.out.println(((SkipMap) map).printTree());
		System.out.println(map.toString());
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
	public void testPollFirstEntry() {
		assertNull(map.pollFirstEntry());
		testPut(true);
		while (!tmap.isEmpty()) {
			assertEquals(tmap.pollFirstEntry(), map.pollFirstEntry());
		}
	}

	@Test
	@Ignore
	public void testPollLastEntry() {
		assertNull(map.pollLastEntry());
		testPut(true);
		while (!tmap.isEmpty()) {
			assertEquals(tmap.pollLastEntry(), map.pollLastEntry());
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

	class Compare implements Comparator<Integer> {
		@Override
		public int compare(Integer i1, Integer i2) {
			return i1.hashCode() - i2.hashCode();
		}
	}
}