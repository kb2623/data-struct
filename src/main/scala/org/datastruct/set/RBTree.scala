package org.datastruct.set

import java.io.{File, PrintWriter}
import scala.collection.mutable

class RBTree[T](
	var root: RBNode[T] = null
)(implicit dataType: T => Ordered[T]) extends mutable.Set[T] {
	private def leftRotate(x: RBNode[T]) {
		var y = x.right
		x.right = y.left
		if (y.left != null) {
			y.left.parent = x
		}
		y.parent = x.parent
		if (x.parent == null) {
			root = y
		} else if (x == x.parent.left) {
			x.parent.left = y
		} else {
			x.parent.right = y
		}
		y.left = x
		x.parent = y
	}

	private def rightRotate(y: RBNode[T]) {
		var x = y.left
		y.left = x.right
		if (x.right != null) {
			x.right.parent = y
		}
		x.parent = y.parent
		if (x.parent == null) {
			root = x
		} else if (y == y.parent.left) {
			y.parent.left = x
		} else {
			y.parent.right = x
		}
		x.right = y
		y.parent = x
	}

	private def rbInsertFixup(zi: RBNode[T]) {
		var z = zi
		var y = root
		while ((z != root) && (z.parent.color == RBNodeColor.RED)) {
			if (z.parent == z.parent.parent.left) {
				y = z.parent.parent.right
				if (y != null && y.color == RBNodeColor.RED) {
					z.parent.color = RBNodeColor.BLACK
					y.color = RBNodeColor.BLACK
					z.parent.parent.color = RBNodeColor.RED
					z = z.parent.parent
				} else {
					if (z == z.parent.right) {
						z = z.parent
						leftRotate(z)
					}
					z.parent.color = RBNodeColor.BLACK
					z.parent.parent.color = RBNodeColor.RED
					rightRotate(z.parent.parent)
				}
			} else {
				y = z.parent.parent.left
				if (y != null && y.color == RBNodeColor.RED) {
					z.parent.color = RBNodeColor.BLACK
					y.color = RBNodeColor.BLACK
					z.parent.parent.color = RBNodeColor.RED
					z = z.parent.parent
				} else {
					if (z == z.parent.left) {
						z = z.parent
						rightRotate(z)
					}
					z.parent.color = RBNodeColor.BLACK
					z.parent.parent.color = RBNodeColor.RED
					leftRotate(z.parent.parent)
				}
			}
		}
		root.color = RBNodeColor.BLACK
	}
	/** Vstavljanje
	  * Casovna zahtevnost: O(log(n))
	  *
	  * @param data
	  * @return
	  */
	override def +=(data: T): RBTree.this.type = {
		var x = root
		var y = x
		while (x != null) {
			y = x
			if (data < x.data) {
				x = x.left
			} else if (data > x.data) {
				x = x.right
			} else {
				throw new IllegalArgumentException("Key " + data + " exists!!!")
			}
		}
		var z = new RBNode[T](data)
		z.parent = y
		if (y == null) {
			root = z
		} else if (z.data < y.data) {
			y.left = z
		} else {
			y.right = z
		}
		rbInsertFixup(z)
		this
	}

	private def rbDeleteFixup(ix: RBNode[T]) {
		var x = ix
		var w = root
		while (root != x && x.color == RBNodeColor.BLACK) {
			if (x == x.parent.left) {
				w = x.parent.right
				if (w.color == RBNodeColor.RED) {
					w.color = RBNodeColor.BLACK
					x.parent.color = RBNodeColor.RED
					leftRotate(x.parent)
					w = x.parent.right
				}
				if (w.left.color == RBNodeColor.BLACK && w.right.color == RBNodeColor.BLACK) {
					w.color = RBNodeColor.RED
					x = x.parent
				} else {
					if (w.right.color == RBNodeColor.BLACK) {
						w.left.color = RBNodeColor.BLACK
						w.color = RBNodeColor.RED
						rightRotate(w)
						w = x.parent.right
					}
					w.color = x.parent.color
					x.parent.color = RBNodeColor.BLACK
					w.right.color = RBNodeColor.BLACK
					leftRotate(x.parent)
					x = root
				}
			} else {
				w = x.parent.left
				if (w.color == RBNodeColor.RED) {
					w.color = RBNodeColor.BLACK
					x.parent.color = RBNodeColor.RED
					rightRotate(x.parent)
					w = x.parent.left
				}
				if (w.right.color == RBNodeColor.BLACK && w.left.color == RBNodeColor.BLACK) {
					w.color = RBNodeColor.RED
					x = x.parent
				} else {
					if (w.left.color == RBNodeColor.BLACK) {
						w.right.color = RBNodeColor.BLACK
						w.color = RBNodeColor.RED
						leftRotate(w)
						w = x.parent.left
					}
					w.color = x.parent.color
					x.parent.color = RBNodeColor.BLACK
					w.left.color = RBNodeColor.BLACK
					rightRotate(x.parent)
					x = root
				}
			}
		}
		x.color = RBNodeColor.BLACK
	}
	/**
	  * Brisanje
	  * Casovna zahtevnost: O(log(n))
	  *
	  * @param data
	  * @return
	  */
	override def -=(data: T): RBTree.this.type = {
		var y = root
		var x = root
		while (x != null && x.data != data) {
			y = x
			if (data < x.data) {
				x = x.left
			} else if (data > x.data) {
				x = x.right
			}
		}
		if (x != null) {
			var z = x
			if (z.left == null || z.right == null) {
				y = z
			} else {
				y = z.right
				while (y.left != null) {
					y = y.left
				}
			}
			if (y.left != null) {
				x = y.left
			} else {
				x = y.right
			}
			if (x != null) {
				x.parent = y.parent
			}
			if (y.parent == null) {
				root = x
			} else {
				if (y == y.parent.left) {
					y.parent.left = x
				} else {
					y.parent.right = x
				}
			}
			if (y != z) {
				z.data = y.data
			}
			if (y.color == RBNodeColor.BLACK) {
				rbDeleteFixup(z)
			}
		}
		this
	}
	/** Brisanje
	  * Casovna zahtevnost: O(log(n))
	  *
	  * @param elem
	  * @return
	  */
	override def contains(elem: T): Boolean = {
		if (isEmpty) return false
		var node = root
		while (node.data != elem) {
			if (node.data > elem) node = node.left
			else node = node.right
			if (node == null) return false
		}
		true
	}

	override def iterator: Iterator[T] = ???

	override def isEmpty: Boolean = if (root != null) false else true

	private def count(node: RBNode[T]): Int = if (node == null) 0 else 1 + count(node.left) + count(node.right)

	override def size = count(root)

	private def toDot(data: StringBuilder, node: RBNode[T], id: Int): (Int, Int) = {
		if (node == null) (-1, -1)
		else {
			data ++= node.toDotNode(id)
			val idL = toDot(data, node.left, id + 1)
			val idR = toDot(data, node.right, if (idL._1 == -1) id + 1 else idL._2 + 1)
			data ++= node.toDotEdge(id, idL._1, idR._1)
			(id, if (idR._2 != -1) idR._2 else if (idL._2 != -1) idL._2 else id)
		}
	}

	def toDot: String = {
		var nodes = new mutable.StringBuilder
		nodes ++= "digraph {\n"
		toDot(nodes, root, 0)
		nodes += '}'
		nodes.toString
	}

	def toDotFile(filename: String) {
		val pw = new PrintWriter(new File(filename))
		pw.write(toDot)
		pw.close
	}
}