package org.datastruct;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class RadixTreeMapTest {

	private RadixTreeMap<String, Integer> intRTree;
	private RadixTreeMap<String, String> tree;

	@Before
	public void setUp() {
		this.intRTree = new RadixTreeMap<>();
		this.tree = new RadixTreeMap<>();
	}

	@Test
	public void testAdd() {
		testAddOK();
		setUp();
		testAddMoreOK();
		setUp();
		testAddNullArgumentExceptions();
		setUp();
		testAddDuplicateKeyException();
	}

	private void testAddOK() {
		this.intRTree.add(23, "test");
		this.intRTree.add(32, "team");
		assertEquals(new Integer(23), intRTree.get("test"));
		assertEquals(new Integer(32), intRTree.get("team"));
	}

	private void testAddMoreOK() {
		this.intRTree.add(1, "romane");
		this.intRTree.add(2, "romanus");
		this.intRTree.add(3, "romulus");
		this.intRTree.add(4, "rubens");
		this.intRTree.add(5, "ruber");
		this.intRTree.add(6, "rubicon");
		this.intRTree.add(7, "rubicudus");
		assertEquals(new Integer(4), this.intRTree.get("rubens"));
		assertEquals(new Integer(7), this.intRTree.get("rubicudus"));
		assertEquals(new Integer(1), this.intRTree.get("romane"));
		assertEquals(new Integer(5), this.intRTree.get("ruber"));
		assertEquals(new Integer(2), this.intRTree.get("romanus"));
		assertEquals(new Integer(6), this.intRTree.get("rubicon"));
		assertEquals(new Integer(3), this.intRTree.get("romulus"));
	}

	private void testAddNullArgumentExceptions() {
		try {
			this.intRTree.add(null, null);
			assert false;
		} catch (NullPointerException e) {
		}
		try {
			this.intRTree.add(null, "hello");
			assert false;
		} catch (NullPointerException e) {
		}
		try {
			this.intRTree.add(12, null);
			assert false;
		} catch (NullPointerException e) {
		}
	}

	private void testAddDuplicateKeyException() {
		testAddMoreOK();
		testAddOK();
		assertNotNull(this.intRTree.add(41, "team"));
		assertNotNull(this.intRTree.add(42, "rubicon"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testPrintTree() {
		testAdd();
		intRTree.printTree();
		setUp();
		testAddMoreOK();
		intRTree.printTree();
		setUp();
		testAddMoreOK();
		testAdd();
		intRTree.printTree();
	}

	@Test
	public void testIsEmpty() {
		assertTrue(intRTree.isEmpty());
		this.intRTree.add(23, "test");
		this.intRTree.add(32, "team");
		assertFalse(intRTree.isEmpty());
	}

	@Test
	public void testRemove() {
		testRemoveOK();
		setUp();
		testRemoveMore();
	}

	private void testRemoveOK() {
		testAddOK();
		assertTrue(this.intRTree.remove("test"));
		assertFalse(this.intRTree.remove("Hello"));
	}

	private void testRemoveMore() {
		testAddMoreOK();
		testAddOK();
		assertTrue(this.intRTree.remove("test"));
		assertTrue(this.intRTree.remove("rubicudus"));
		assertFalse(this.intRTree.remove("rubers"));
		assertTrue(this.intRTree.remove("romanus"));
		assertFalse(this.intRTree.remove("test"));
		assertTrue(this.intRTree.remove("team"));
		assertFalse(this.intRTree.remove("team"));
		assertTrue(this.intRTree.remove("romane"));
		assertTrue(this.intRTree.remove("romulus"));
		assertTrue(this.intRTree.remove("rubicon"));
		assertTrue(this.intRTree.remove("ruber"));
		assertTrue(this.intRTree.remove("rubens"));
	}

	@Test
	public void testRemoveObject() {
		testRemoveOKObject();
		setUp();
		testRemoveMoreObject();
	}

	private void testRemoveOKObject() {
		testAddOK();
		assertEquals(new Integer(23), ((Map) intRTree).remove("test"));
		assertNull(((Map) intRTree).remove("Hello"));
	}

	private void testRemoveMoreObject() {
		testAddMoreOK();
		testAddOK();
		assertEquals(new Integer(23), ((Map) intRTree).remove("test"));
		assertEquals(new Integer(7), ((Map) intRTree).remove("rubicudus"));
		assertNull(((Map) intRTree).remove("rubers"));
		assertEquals(new Integer(2), ((Map) intRTree).remove("romanus"));
		assertNull(((Map) intRTree).remove("test"));
		assertEquals(new Integer(32), ((Map) intRTree).remove("team"));
		assertNull(((Map) intRTree).remove("team"));
		assertEquals(new Integer(1), ((Map) intRTree).remove("romane"));
		assertEquals(new Integer(3), ((Map) intRTree).remove("romulus"));
		assertEquals(new Integer(6), ((Map) intRTree).remove("rubicon"));
		assertEquals(new Integer(5), ((Map) intRTree).remove("ruber"));
		assertEquals(new Integer(4), ((Map) intRTree).remove("rubens"));
	}

	@Test
	public void testCount() {
		testCountOne();
		setUp();
		testCountTwo();
		setUp();
		testCountThree();
	}

	private void testCountOne() {
		testAddOK();
		assertEquals(2, this.intRTree.count());
	}

	private void testCountTwo() {
		testAddMoreOK();
		assertEquals(7, this.intRTree.count());
	}

	private void testCountThree() {
		testAddOK();
		testAddMoreOK();
		assertEquals(9, this.intRTree.count());
	}

	@Test
	public void testKeySet() {
		testKeySetOne();
		setUp();
		testKeySetTwo();
		setUp();
		testKeySetThree();
	}

	private void testKeySetOne() {
		testAddOK();
		assertEquals("[test, team]", intRTree.keySet().toString());
	}

	private void testKeySetTwo() {
		testAddMoreOK();
		assertEquals("[ruber, romulus, romanus, rubicudus, rubicon, rubens, romane]", intRTree.keySet().toString());
	}

	private void testKeySetThree() {
		testAddMoreOK();
		testAddOK();
		assertEquals("[ruber, romulus, test, romanus, rubicudus, team, rubicon, rubens, romane]", intRTree.keySet().toString());
	}

	@Test
	public void testAsList() {
		testAsListOne();
		setUp();
		testAsListTwo();
		setUp();
		testAsListThree();
	}

	private void testAsListOne() {
		testAddOK();
		assertEquals("[23, 32]", this.intRTree.asList().toString());
	}

	private void testAsListTwo() {
		testAddMoreOK();
		assertEquals("[7, 6, 5, 4, 3, 2, 1]", this.intRTree.asList().toString());
	}

	private void testAsListThree() {
		testAddMoreOK();
		testAddOK();
		assertEquals("[23, 32, 7, 6, 5, 4, 3, 2, 1]", this.intRTree.asList().toString());
	}

	private void addSequenceOne() {
		tree.add("abcd", "abcd");
		tree.add("abce", "abce");
	}

	@Test
	public void testSearchForPartialParentAndLeafKeyWhenOverlapExists() {
		addSequenceOne();
		assertNull(tree.get("abe"));
		assertNull(tree.get("abd"));
	}

	@Test
	public void testSearchForLeafNodesWhenOverlapExists() {
		addSequenceOne();
		assertEquals("abcd", tree.get("abcd"));
		assertEquals("abce", tree.get("abce"));
	}

	@Test
	public void testSearchForStringSmallerThanSharedParentWhenOverlapExists() {
		addSequenceOne();
		assertNull(tree.get("abc"));
		assertNull(tree.get("ab"));
		assertNull(tree.get("a"));
	}

	@Test
	public void testAddWithString() {
		tree.add("apple", "apple");
		tree.add("bat", "bat");
		tree.add("ape", "ape");
		tree.add("bath", "bath");
		tree.add("banana", "banana");
		assertEquals("apple", tree.get("apple"));
		assertEquals("bat", tree.get("bat"));
		assertEquals("ape", tree.get("ape"));
		assertEquals("bath", tree.get("bath"));
		assertEquals("banana", tree.get("banana"));
	}

	@Test
	public void testAddExistingUnrealNodeConvertsItToReal() {
		tree.add("applepie", "applepie");
		tree.add("applecrisp", "applecrisp");
		assertNull(tree.get("apple"));
		tree.add("apple", "apple");
		assertEquals("apple", tree.get("apple"));
	}

	@Test
	public void testDuplicates() {
		assertNull(tree.add("apple", "apple"));
		assertNotNull(tree.add("appleOne", "apple"));
	}

	@Test
	public void testAddWithRepeatingPatternsInKey() {
		tree.add("xbox 360", "xbox 360");
		tree.add("xbox", "xbox");
		tree.add("xbox 360 games", "xbox 360 games");
		tree.add("xbox games", "xbox games");
		tree.add("xbox xbox 360", "xbox xbox 360");
		tree.add("xbox xbox", "xbox xbox");
		tree.add("xbox 360 xbox games", "xbox 360 xbox games");
		tree.add("xbox games 360", "xbox games 360");
		tree.add("xbox 360 360", "xbox 360 360");
		tree.add("xbox 360 xbox 360", "xbox 360 xbox 360");
		tree.add("360 xbox games 360", "360 xbox games 360");
		tree.add("xbox xbox 361", "xbox xbox 361");
		assertEquals(12, tree.count());
	}

	@Test
	public void testRemoveNodeWithNoChildren() {
		tree.add("apple", "apple");
		assertTrue(tree.remove("apple"));
	}

	@Test
	public void testRemoveNodeWithOneChild() {
		tree.add("apple", "apple");
		tree.add("applepie", "applepie");
		assertTrue(tree.remove("apple"));
		assertNull(tree.get("apple"));
		assertEquals("applepie", tree.get("applepie"));
	}

	@Test
	public void testRemoveNodeWithOneChildObject() {
		((Map) tree).put("apple", "apple");
		((Map) tree).put("applepie", "applepie");
		assertEquals("apple", ((Map) tree).remove("apple"));
		assertNull(((Map) tree).get("apple"));
		assertEquals("applepie", ((Map) tree).get("applepie"));
	}

	@Test
	public void testRemoveNodeWithMultipleChildren() {
		tree.add("apple", "apple");
		tree.add("applepie", "applepie");
		tree.add("applecrisp", "applecrisp");
		assertTrue(tree.remove("apple"));
		assertNull(tree.get("apple"));
		assertEquals("applepie", tree.get("applepie"));
		assertEquals("applecrisp", tree.get("applecrisp"));
	}

	@Test
	public void testRemoveNodeWithMultipleChildrenObject() {
		((Map) tree).put("apple", "apple");
		((Map) tree).put("applepie", "applepie");
		((Map) tree).put("applecrisp", "applecrisp");
		assertEquals("apple", ((Map) tree).remove("apple"));
		assertNull(((Map) tree).get("apple"));
		assertEquals("applepie", ((Map) tree).get("applepie"));
		assertEquals("applecrisp", ((Map) tree).get("applecrisp"));
	}

	@Test
	public void testCantDeleteSomethingThatDoesntExist() {
		assertFalse(tree.remove("apple"));
	}

	@Test
	public void testCantDeleteSomethingThatDoesntExistObject() {
		assertNull(((Map) tree).remove("apple"));
	}

	@Test
	public void testCantDeleteSomethingThatWasAlreadyDeleted() {
		tree.add("apple", "apple");
		assertTrue(tree.remove("apple"));
		assertFalse(tree.remove("apple"));
	}

	@Test
	public void testCantDeleteSomethingThatWasAlreadyDeletedObject() {
		((Map) tree).put("apple", "apple");
		assertEquals("apple", ((Map) tree).remove("apple"));
		assertNull(((Map) tree).remove("apple"));
	}

	@Test
	public void testChildrenNotAffectedWhenOneIsDeleted() {
		tree.add("apple", "apple");
		tree.add("appleshack", "appleshack");
		tree.add("applepie", "applepie");
		tree.add("ape", "ape");
		assertTrue(tree.remove("apple"));
		assertEquals("appleshack", tree.get("appleshack"));
		assertEquals("applepie", tree.get("applepie"));
		assertEquals("ape", tree.get("ape"));
		assertNull(tree.get("apple"));
	}

	@Test
	public void testChildrenNotAffectedWhenOneIsDeletedObject() {
		((Map) tree).put("apple", "apple");
		((Map) tree).put("appleshack", "appleshack");
		((Map) tree).put("applepie", "applepie");
		((Map) tree).put("ape", "ape");
		assertEquals("apple", ((Map) tree).remove("apple"));
		assertEquals("appleshack", ((Map) tree).get("appleshack"));
		assertEquals("applepie", ((Map) tree).get("applepie"));
		assertEquals("ape", ((Map) tree).get("ape"));
		assertNull(((Map) tree).get("apple"));
	}

	@Test
	public void testSiblingsNotAffectedWhenOneIsDeleted() {
		tree.add("apple", "apple");
		tree.add("ball", "ball");
		assertTrue(tree.remove("apple"));
		assertEquals("ball", tree.get("ball"));
	}

	@Test
	public void testSiblingsNotAffectedWhenOneIsDeletedObject() {
		((Map) tree).put("apple", "apple");
		((Map) tree).put("ball", "ball");
		assertEquals("apple", ((Map) tree).remove("apple"));
		assertEquals("ball", ((Map) tree).get("ball"));
	}

	@Test
	public void testCantRemoveUnrealNode() {
		tree.add("apple", "apple");
		tree.add("ape", "ape");
		assertFalse(tree.remove("ap"));
	}

	@Test
	public void testCantRemoveUnrealNodeObject() {
		((Map) tree).put("apple", "apple");
		((Map) tree).put("ape", "ape");
		assertNull(((Map) tree).remove("ap"));
	}

	@Test
	public void testCantFindRootNode() {
		assertNull(tree.get(""));
	}

	@Test
	public void testFindSimpleInsert() {
		tree.add("apple", "apple");
		assertEquals("apple", tree.get("apple"));
	}

	@Test
	public void testCantFindNonexistantNode() {
		assertNull(tree.get("apple"));
	}

	@Test
	public void testCantFindUnrealNode() {
		tree.add("apple", "apple");
		tree.add("ape", "ape");
		assertNull(tree.get("ap"));
	}

	@Test
	public void testGetSize() {
		tree.add("apple", "apple");
		tree.add("appleshack", "appleshack");
		tree.add("appleshackcream", "appleshackcream");
		tree.add("applepie", "applepie");
		tree.add("ape", "ape");
		assertEquals(5, tree.count());
	}

	@Test
	public void testDeleteReducesSize() {
		tree.add("apple", "apple");
		tree.add("appleshack", "appleshack");
		tree.remove("appleshack");
		assertEquals(1, tree.count());
	}

	@Test
	public void testDeleteReducesSizeObject() {
		((Map) tree).put("apple", "apple");
		((Map) tree).put("appleshack", "appleshack");
		((Map) tree).remove("appleshack");
		assertEquals(1, tree.count());
	}

	@Test
	public void testAddOne() {
		tree.add("applepie", "applepie");
		tree.add("appleshack", "appleshack");
		tree.add("apple", "apple");
		assertNotNull(tree.get("apple"));
		assertNotNull(tree.get("applepie"));
	}

	@Test
	public void testIterator() {
		testIteratorOne();
		setUp();
		testIteratorTwo();
		setUp();
		testIteratorThree();
	}

	private void testIteratorOne() {
		testAddOK();
		Iterator<Integer> it = this.intRTree.iterator();
		assertEquals(new Integer(32), it.next());
		assertEquals(new Integer(23), it.next());
	}

	private void testIteratorTwo() {
		String niz = "";
		testAddMoreOK();
		for (Integer anInsRadixTree : this.intRTree) {
			niz += " " + anInsRadixTree.toString();
		}
		assertEquals(" 1 2 3 4 5 6 7", niz);
	}

	private void testIteratorThree() {
		String niz = "";
		testAddOK();
		testAddMoreOK();
		for (Integer anInsRadixTree : this.intRTree) {
			niz += " " + anInsRadixTree.toString();
		}
		assertEquals(" 1 2 3 4 5 6 7 32 23", niz);
	}

	@Test
	public void testEntrySet() {
		testEntrySetOne();
		setUp();
		testEntrySetTwo();
		setUp();
		testEntrySetThree();
	}

	private void testEntrySetOne() {
		testAddOK();
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		intRTree.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(',').append(e.getValue()).append(']'));
		builder.append(']');
		assertEquals(new Integer(32), intRTree.get("team"));
		assertEquals(new Integer(23), intRTree.get("test"));
	}

	private void testEntrySetTwo() {
		testAddMoreOK();
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		intRTree.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(',').append(e.getValue()).append(']'));
		builder.append(']');
		assertEquals("[[ruber,5][romulus,3][romanus,2][rubicudus,7][rubicon,6][rubens,4][romane,1]]", builder.toString());
	}

	private void testEntrySetThree() {
		testAddMoreOK();
		testAddOK();
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		intRTree.entrySet().forEach(e -> builder.append('[').append(e.getKey()).append(',').append(e.getValue()).append(']'));
		builder.append(']');
		assertEquals("[[ruber,5][romulus,3][test,23][romanus,2][rubicudus,7][team,32][rubicon,6][rubens,4][romane,1]]", builder.toString());
	}
}

