package org.datastruct.set

import scala.collection.mutable

class BTree[T](
	var root: BNode[T] = null,
	val degree: Int
)(implicit dataType: T => Ordered[T]) extends mutable.Set[T] {
	private def splitChild(x: BNode[T], i: Int, y: BNode[T]) = ???

	private def insertNonFull(x: BNode[T], k: Int) = ???

	override def +=(elem: T): BTree.this.type = ???

	override def -=(elem: T): BTree.this.type = ???

	override def contains(elem: T): Boolean = ???

	override def iterator: Iterator[T] = ???
}
