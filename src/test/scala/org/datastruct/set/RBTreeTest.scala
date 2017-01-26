package org.datastruct.set

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import collection.mutable.Stack
import org.scalatest._

@RunWith(classOf[JUnitRunner])
class RBTreeTest extends FlatSpec {

	"A Stack" should "pop values in last-in-first-out order" in {
		val stack = new Stack[Int]
		stack.push(1)
		stack.push(2)
		assert(stack.pop() === 2)
		assert(stack.pop() === 1)
	}

	it should "throw NoSuchElementException if an empty stack is popped" in {
		val emptyStack = new Stack[String]
		assertThrows[NoSuchElementException] {
			emptyStack.pop()
		}
	}
}
