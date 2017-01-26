package org.datastruct.set

import scala.collection.mutable

class BTree[T <: Ordered[T]] extends mutable.Set[T] {

	case class Node[T <: Ordered[T]] (
		var data: Array[T],
		var chields: Array[BTree[T]#Node[T]]
	)

	var root: BTree[T]#Node[T] = null
	var degree: Int = 5

	def this(root: BTree[T]#Node[T] = null, degree: Int = 5) {
		this()
		this.root = root
		this.degree = degree
	}

	private def splitChild(x: BTree[T]#Node[T], i: Int, y: BTree[T]#Node[T]) = ???

	private def insertNonFull(x: BTree[T]#Node[T], k: Int) = ???

	override def +=(elem: T): BTree.this.type = ???

	override def -=(elem: T): BTree.this.type = ???

	override def contains(elem: T): Boolean = ???

	override def iterator: Iterator[T] = ???
}
