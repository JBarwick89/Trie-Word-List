/*
 * Implements a set using a binary search tree as the underlying data structure. 
 */

public class BSTSet<E extends Comparable<E>> {
	private BinarySearchTree<E> data = new BinarySearchTree<>(); // where the set data is kept

	// Adds the specified newItem to the set. 
	public void add(E newItem) {
		data.add(newItem);
	}

	// Removes and returns the specified someItem from the set. Returns null
	// if someItem doesn't exist in the set.
	public E remove(E someItem) {
		return data.delete(someItem);
	}

	// Returns whether the set contains the specified someItem.
	public boolean contains(E someItem) {
		return data.find(someItem) != null;
	}

	public String toString() {
		String r = "Set object containing these elements: {";
		for (E item : data) {
			r += item + ", ";
		}

		r = r.substring(0, r.length() - 2); // cut off the last comma and space
		r += "}";
		return r;
	}

	public static void main(String[] args) {
		//Creates and tests a BSTSet
		BSTSet<String> s = new BSTSet<>();
		String[] stuff = { "Piper Cub", "Paper airplane", "B-52", "AC-130",
				"F-22", "Flying shark with laser", "X-wing starfighter",
				"Death Star" };
		for (String item : stuff) {
			s.add(item);
		}
		System.out.println(s);
	}
}
