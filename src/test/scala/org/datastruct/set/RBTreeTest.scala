package org.datastruct.set

import junit.framework.Assert
import org.junit.{After, Before, Test}

class RBTreeTest {

	val tree = new RBTree[Int]()

	@Before
	def setUp() {
	}

	@After
	def tearDown() {
	}

	@Test
	def testOne() {
		tree ++= List(2, 30, 1, 20, 0, 10, 22, 33, 65)
	}

}
