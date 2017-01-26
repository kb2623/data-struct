package org.datastruct.set

case class RBNode[T] (
	var data: T,
	var parent: RBNode[T] = null,
	var left: RBNode[T] = null,
	var right: RBNode[T] = null,
	var color: Boolean = RBNodeColor.RED
)(implicit dataType: T => Ordered[T]) {
	def toDotNode(id: Int) = "S" + id + " [style=filled, label=\"" + data.toString + "\", fillcolor=" + (if (color == RBNodeColor.RED) "red" else "black") + ", shape=circle, fontcolor=yellow];\n"

	def toDotEdge(ids: Int*) = (if (ids(1) != -1) "S" + ids(0) + " -> S" + ids(1) + ";\n" else "") + (if (ids(2) != -1) "S" + ids(0) + " -> S" + ids(2) + ";\n" else "")
}
