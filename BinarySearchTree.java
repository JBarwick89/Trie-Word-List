import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/*
 * Implementation of a binary search tree.
 */

// The "extends Comparable<E>" in the type parameter means that E must be from a class that implements
//  the Comparable interface (located in java.lang).  This is needed since you must be able to "order"
//  elements in some way to place them into a BST.

public class BinarySearchTree<E extends Comparable<E>> implements Iterable<E> {
	
	// Nested class representing one node of the tree. Very similar to the Node
	// class we used for LinkedList -- the only difference is that we keep
	// track of two children instead of a single "next" node.
	
	private static class Node<E> {
		private E data;
		private Node<E> left, right;

		public Node(E data, Node<E> left, Node<E> right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}
	}

	// Nested class that implements an in-order iterator over the BST. We
	// maintain the
	// current node where the iterator is, along with a stack to track which
	// nodes still
	// need to be visited.
	
	private class InOrderIterator implements Iterator<E> {
		private Node<E> current = root;
		private Stack<Node<E>> s = new Stack<>();

		// Returns whether there is a next element in the iteration.
		@Override
		public boolean hasNext() {
			return !(current == null && s.isEmpty());
		}

		// Returns the next element in the iteration, and advances the
		// iterator to the following element.
		@Override
		public E next() {
			if (hasNext()) {
				while (current != null) {
					s.push(current);
					current = current.left;
				}
				Node<E> popped = s.pop();
				current = popped.right;
				return popped.data;
			} else
				throw new NoSuchElementException();
		}

		// Removes the last element returned from calling next().
		// (Not supported in this implementation)
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private Node<E> root;
	private E deleteReturn; // tracks which item was just deleted from the tree
							// (so that the delete method can return something)

	// Returns an iterator over this BST. Required for the class to implement
	// Iterable.
	public Iterator<E> iterator() {
		return new InOrderIterator();
	}

	// Wrapper method for pre-order traversal.
	public void preOrderTraverse() {
		preOrderTraverse(root);
	}

	// Pre-order traverses the tree, starting from the node where.
	private void preOrderTraverse(Node<E> where) {
		if (where != null) { // base case is when where == null... do nothing
			System.out.println(where.data);
			preOrderTraverse(where.left);
			preOrderTraverse(where.right);
		}
	}

	// Wrapper method for in-order traversal.
	public void inOrderTraverse() {
		inOrderTraverse(root);
	}

	// In-order traverses the tree, starting from the node where.
	private void inOrderTraverse(Node<E> where) {
		if (where != null) { // base case is when where == null... do nothing
			inOrderTraverse(where.left);
			System.out.println(where.data);
			inOrderTraverse(where.right);
		}
	}

	// Wrapper method for add
	public void add(E newItem) {
		if (root == null) // special case for adding to the root (we need to
							// modify the root reference)
			root = new Node<E>(newItem, null, null);
		else
			add(newItem, root);
	}

	// Recursively adds newItem to the tree rooted at where.
	private void add(E newItem, Node<E> where) {
		// remember that a.compareTo(b) returns 0 if a is "equal to" b, a
		// negative value if a is "less than" b, or
		// a positive value if a is "greater than" b
		int compare = newItem.compareTo(where.data);
		if (compare < 0 && where.left == null) // base case - add a new left
												// child to where
			where.left = new Node<E>(newItem, null, null);
		else if (compare > 0 && where.right == null) // base case - add a new
														// right child to where
			where.right = new Node<E>(newItem, null, null);
		else if (compare < 0)
			add(newItem, where.left); // recursively add newItem to where's left
										// subtree
		else if (compare > 0)
			add(newItem, where.right); // recursively add newItem to where's
										// right subtree

		// (do nothing if compare == 0... we don't allow duplicate elements in
		// the tree)
	}

	// Alternate version of add (the way it's done in the textbook).
	public void add2(E newItem) {
		root = add2(newItem, root);
	}

	// Recursively adds the newItem into the tree rooted at where. Returns a
	// reference to the root of that tree, with the newItem added.
	private Node<E> add2(E newItem, Node<E> where) {
		if (where == null) {
			return new Node<E>(newItem, null, null);
		} else {
			int compare = newItem.compareTo(where.data);
			if (compare < 0) {
				where.left = add2(newItem, where.left);
			} else if (compare > 0) {
				where.right = add2(newItem, where.right);
			}
			return where;
		}
	}

	// Wrapper method for find.
	public E find(E someItem) {
		return find(someItem, root);
	}

	// Recursively searches the tree rooted at where for someItem. Returns null
	// if item not found,
	// or the matching item from the tree if found.
	private E find(E someItem, Node<E> where) {
		if (where == null) // base case - tree is empty
			return null;
		else {
			int compare = someItem.compareTo(where.data);

			if (compare == 0) // base case - someItem found
				return where.data;
			else if (compare < 0) // recursively search where's left subtree for
									// someItem
				return find(someItem, where.left);
			else
				// recursively search where's right subtree for someItem
				return find(someItem, where.right);
		}
	}

	// Wrapper method for delete. Returns the item that was deleted if found, or
	// null if item not found.
	public E delete(E someItem) {
		root = delete(someItem, root);
		return deleteReturn;
	}

	// Recursively deletes someItem from the tree rooted at where. Returns a
	// reference to the root of that tree, with someItem deleted.
	private Node<E> delete(E someItem, Node<E> where) {
		if (where == null) { // base case - empty tree
			deleteReturn = null;
			return null;
		}

		int compare = someItem.compareTo(where.data);
		if (compare < 0) {
			where.left = delete(someItem, where.left);
			return where;
		} else if (compare > 0) {
			where.right = delete(someItem, where.right);
			return where;
		} else { // base cases - someItem found
			deleteReturn = where.data;
			if (where.left == null && where.right == null) { // case 1 - where
																// has no
																// children
				return null;
			} else if (where.right == null) { // case 2a - where has only a left
												// child
				return where.left;
			} else if (where.left == null) { // case 2b - where has only a right
												// child
				return where.right;
			} else { // case 3 - oh noes! two children
				where.data = findAndDeleteIOP(where);
				return where;
			}
		}
	}

	// Finds and deletes the in-order predecessor of the node where. Returns the
	// value of
	// the IOP that was deleted.
	private E findAndDeleteIOP(Node<E> where) {
		Node<E> parent = where, temp = where.left;

		// at the end of this loop, temp is pointing at the IOP, and parent is
		// pointing
		// at the IOP's parent
		while (temp.right != null) {
			parent = temp;
			temp = temp.right;
		}

		if (parent == where) // if parent and temp did not move down the tree at
								// all (i.e., the IOP is where.left)
			parent.left = temp.left;
		else
			parent.right = temp.left;
		return temp.data;
	}

	// Wrapper method for toString
	public String toString() {
		return toString(root, "");
	}

	// Recursive version of toString
	private String toString(Node<E> where, String offset) {
		if (where == null)
			return offset + "null";
		else
			return offset + where.data + "\n"
					+ toString(where.left, offset + " ") + "\n"
					+ toString(where.right, offset + " ");
	}

	public void addIterative(E newItem) {
		if (root == null) { // exception for if the tree is empty. Then you just
							// make the the new node the root.
			root = new Node<E>(newItem, null, null);
		} else {
			Node<E> temp = root;

			while (true) { // 5 cases
				int comparison = newItem.compareTo(temp.data);
				if (comparison == 0) { // 1) item is a duplicate of current node
										// and should not be added
					break;
				} else if (comparison < 0 && temp.left == null) { // 2) item is
																	// less than
																	// current
																	// node and
																	// should be
																	// its new
																	// left
																	// child
					temp.left = new Node<E>(newItem, null, null);
					break;
				} else if (comparison > 0 && temp.right == null) { // 3) item is
																	// greater
																	// than
																	// current
																	// node and
																	// should be
																	// its new
																	// right
																	// child
					temp.right = new Node<E>(newItem, null, null);
					break;
				} else if (comparison < 0) { // 4) item is less than current
												// node, and search continues in
												// current node's left subtree
					temp = temp.left;
				} else { // 5) item is greater than current node, and search
							// continues in current node's right subtree
					temp = temp.right;
				}
			}
		}
	}

	public E findIterative(E someItem) {
		Node<E> temp = root;

		while (true) { // 5 cases
			int comparison = someItem.compareTo(temp.data);
			if (comparison == 0) { // 1) item is a duplicate of current node and
									// should not be added
				return temp.data;
			} else if (comparison < 0 && temp.left == null) { // 2) item is less
																// than current
																// node and
																// should be its
																// new left
																// child
				return null;
			} else if (comparison > 0 && temp.right == null) { // 3) item is
																// greater than
																// current node
																// and should be
																// its new right
																// child
				return null;
			} else if (comparison < 0) { // 4) item is less than current node,
											// and search continues in current
											// node's left subtree
				temp = temp.left;
			} else { // 5) item is greater than current node, and search
						// continues in current node's right subtree
				temp = temp.right;
			}
		}
	}

	public static void main(String[] args) {
		
		//Creates and tests a BinarySearchTree
		BinarySearchTree<String> bst = new BinarySearchTree<>();
		String[] stuff = { "Prius", "Aston Martin Vanquish",
				"Corvette C7 Stingray", "El Camino", "Panzer tank",
				"M1A1 Abrams", "Optimus Prime" };
		for (String s : stuff)
			bst.add2(s);

		for (String s : bst) {
			System.out.println(s);
		}

	}
}
