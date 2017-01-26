package org.datastruct.set

case class BNode[T] (
	var data: Array[T],
	var chields: Array[T]
)(implicit dataType: T => Ordered[T])
