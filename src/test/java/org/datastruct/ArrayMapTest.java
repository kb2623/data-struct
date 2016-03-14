package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ArrayMapTest {

	private Map<Integer, Integer> map;
	private SortedMap<Integer, Integer> smap;

	@Before
	public void setUp() {
		map = new ArrayMap<>(10, .5f);
		smap = new TreeMap<>(new Compare());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitOneArgZero() {
		new ArrayMap<>(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitOneArgNegative() {
		new ArrayMap<>(-10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsZero() {
		new ArrayMap<>(0, 2f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsNegative() {
		new ArrayMap<>(-12, 2f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsZeroTwo() {
		new ArrayMap<>(100, 0f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsNegativeTwo() {
		new ArrayMap<>(200, -.34f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentsInitTwoArgsOverTwo() {
		new ArrayMap<>(20, 1.01f);
	}

	@Test
	public void testSize() throws Exception {
		map.put(1, 1);
		assertEquals(1, map.size());
		map.put(2, 2);
		assertEquals(2, map.size());
		map.put(3, 3);
		assertEquals(3, map.size());
		map.put(4, 4);
		assertEquals(4, map.size());
		map.put(5, 5);
		assertEquals(5, map.size());
		map.put(5, 5);
		assertEquals(5, map.size());
		map.put(6, 6);
		assertEquals(6, map.size());
		map.put(7, 7);
		assertEquals(7, map.size());
		map.put(5, 5);
		assertEquals(7, map.size());
		map.put(8, 8);
		assertEquals(8, map.size());
		map.put(9, 9);
		assertEquals(9, map.size());
		map.put(10, 10);
		assertEquals(10, map.size());
		map.clear();
		assertEquals(0, map.size());
	}

	@Test
	public void testIsEmpty() throws Exception {
		assertTrue(map.isEmpty());
		testSize();
		assertTrue(map.isEmpty());
		map.put(1, 1);
		assertFalse(map.isEmpty());
		map.put(1, 1);
		assertFalse(map.isEmpty());
		map.put(1, 1);
		assertFalse(map.isEmpty());
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testContainsKey() {
		testPutNoLimits();
		assertTrue(map.containsKey(new Integer(1)));
		assertFalse(map.containsKey(new Integer(100)));
		assertTrue(map.containsKey(new Integer(15)));
		assertFalse(map.containsKey(new Integer(16)));
	}

	@Test(expected = NullPointerException.class)
	public void testContainsKeyNullPointerException() {
		testPutNoLimits();
		map.containsKey(null);
	}

	@Test
	public void testContainsValue() {
		testPutNoLimits();
		assertFalse(map.containsValue(new Integer(100)));
		assertTrue(map.containsValue(new Integer(15)));
		assertTrue(map.containsValue(new Integer(14)));
		assertFalse(map.containsValue(new Integer(-10)));
	}

	@Test(expected = NullPointerException.class)
	public void testContaisnValueNullPointerException() {
		testPutNoLimits();
		map.containsValue(null);
	}

	@Test
	public void testGet() {
		testPutNoLimits();
		assertEquals(new Integer(1), map.get(new Integer(1)));
		assertNull(map.get(new Integer(20)));
		assertEquals(new Integer(14), map.get(new Integer(14)));
		assertNull(map.get(new Integer(100)));
	}

	@Test(expected = NullPointerException.class)
	public void testGetNullPointerException() {
		testPutNoLimits();
		map.get(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPutLimits() {
		map = new ArrayMap<>(10);
		assertNull(map.put(1, 1));
		assertNull(map.put(2, 2));
		assertNull(map.put(3, 3));
		assertNull(map.put(4, 4));
		assertNull(map.put(5, 5));
		assertNull(map.put(6, 6));
		assertNull(map.put(7, 7));
		assertNull(map.put(8, 8));
		assertNull(map.put(9, 9));
		assertNull(map.put(10, 10));
		assertEquals(10, map.size());
		assertEquals(new Integer(1), map.put(1, 3));
		assertEquals(new Integer(5), map.put(5, 20));
		assertEquals(new Integer(10), map.put(10, 100));
		assertEquals(10, map.size());
		assertEquals(new Integer(3), map.put(1, 1));
		assertEquals(new Integer(20), map.put(5, 5));
		assertEquals(new Integer(100), map.put(10, 10));
		map.put(11, 11);
	}

	@Test
	public void testPutNoLimits() {
		assertNull(map.put(1, 1));
		assertNull(map.put(2, 2));
		assertNull(map.put(3, 3));
		assertNull(map.put(4, 4));
		assertNull(map.put(5, 5));
		assertNull(map.put(6, 6));
		assertNull(map.put(7, 7));
		assertNull(map.put(8, 8));
		assertNull(map.put(9, 9));
		assertNull(map.put(10, 10));
		assertEquals(10, map.size());
		assertEquals(new Integer(1), map.put(1, 3));
		assertEquals(new Integer(5), map.put(5, 20));
		assertEquals(new Integer(10), map.put(10, 100));
		assertEquals(10, map.size());
		assertEquals(new Integer(3), map.put(1, 1));
		assertEquals(new Integer(20), map.put(5, 5));
		assertEquals(new Integer(100), map.put(10, 10));
		assertNull(map.put(11, 11));
		assertNull(map.put(12, 12));
		assertNull(map.put(13, 13));
		assertNull(map.put(14, 14));
		assertNull(map.put(15, 15));
		assertEquals(15, map.size());
	}

	@Test(expected = NullPointerException.class)
	public void testPutNullPointerExceptionKey() {
		map.put(null, 20);
	}

	@Test(expected = NullPointerException.class)
	public void testPutNullPointerExceptionValue() {
		map.put(20, null);
	}

	@Test
	public void testRemove() {
		testPutNoLimits();
		assertFalse(map.isEmpty());
		assertNull(map.remove(new Integer(20)));
		assertEquals(new Integer(1), map.remove(new Integer(1)));
		assertEquals(14, map.size());
		assertEquals(new Integer(14), map.remove(new Integer(14)));
		assertEquals(13, map.size());
		assertEquals(new Integer(2), map.remove(new Integer(2)));
		assertEquals(new Integer(5), map.remove(new Integer(5)));
		assertEquals(new Integer(3), map.remove(new Integer(3)));
		assertEquals(new Integer(4), map.remove(new Integer(4)));
		assertEquals(new Integer(7), map.remove(new Integer(7)));
		assertEquals(new Integer(11), map.remove(new Integer(11)));
		assertEquals(new Integer(6), map.remove(new Integer(6)));
		assertEquals(new Integer(8), map.remove(new Integer(8)));
		assertEquals(new Integer(9), map.remove(new Integer(9)));
		assertEquals(new Integer(10), map.remove(new Integer(10)));
		assertEquals(new Integer(12), map.remove(new Integer(12)));
		assertEquals(new Integer(13), map.remove(new Integer(13)));
		assertNull(map.remove(new Integer(14)));
		assertEquals(new Integer(15), map.remove(new Integer(15)));
		assertTrue(map.isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void testRemoveNullPointerException() {
		testPutNoLimits();
		map.remove(null);
	}

	@Test
	public void testPutAll() {

	}

	@Test(expected = NullPointerException.class)
	public void testPutAllNullPointerException() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 2);
		map.put(null, 2);
		this.map.putAll(map);
	}

	@Test
	public void testClear() {
		assertTrue(map.isEmpty());
		testPutNoLimits();
		map.clear();
		assertTrue(map.isEmpty());
	}

	@Test
	public void testKeySet() {
		testPutNoLimits();
		Set<Integer> set = map.keySet();
		// TODO
	}

	@Test
	public void testValues() {
		testPutNoLimits();
		Collection<Integer> c = map.values();
		assertTrue(c.contains(new Integer(1)));
		assertTrue(c.contains(new Integer(15)));
		assertTrue(c.contains(new Integer(3)));
		assertFalse(c.contains(new Integer(20)));
	}

	@Test
	public void testEntrySet() {
		testPutNoLimits();
		Set<Map.Entry<Integer, Integer>> set = map.entrySet();
		// TODO
	}

	class Compare implements Comparator<Integer> {
		@Override
		public int compare(Integer integer, Integer t1) {
			return integer.hashCode() - t1.hashCode();
		}
	}
}