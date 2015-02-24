import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// Implements a SpellChecker/Dictionary using a trie data structure.
public class TrieSpellChecker {

	Node root = new Node(' ');

	// The node class contains a character, children represented by an
	// ArrayList, and a boolean value to track if each node is shaded. Shaded nodes indicate that the node
	// is the last letter in a word.
	private static class Node {
		private char data;
		private ArrayList<Node> children = new ArrayList<>();
		private boolean isShaded;

		public Node(char data) {
			this.data = data;
			isShaded = false;
		}

		public void setIsShaded(boolean b) {
			isShaded = b;
		}
	}

	// takes the string argument s and adds its characters to the trie.
	public void add(String s) {
		Node temp = root; // The temp node is set to the root, which is empty.
		char currentLetter;
		boolean nodeCheck = false; // NodeCheck will track whether a child
									// contains the current character.

		for (int i = 0; i < s.length(); i++) {
			currentLetter = s.charAt(i);
			nodeCheck = false;
			// As the loop iterates, if the current letter in the string is
			// found, temp becomes that node.
			for (Node j : temp.children) {
				if (j.data == currentLetter) {
					temp = j;
					nodeCheck = true;
				}
			}

			// If after that initial loop, nodeCheck remains false, then we
			// create a new node.
			if (nodeCheck == false) {
				Node nextTemp = new Node(currentLetter);
				temp.children.add(nextTemp);
				temp = nextTemp;
			}
		}
		// Once we reach the end of the string, thus the end of the word, that
		// node is shaded.
		temp.setIsShaded(true);
	}

	// Contains checks the trie to see if that word can be made with the
	// characters in the trie.
	public boolean contains(String s) {
		Node temp = root;
		boolean foundChild = false;

		// Iterate through the string by character, checking to see if a child
		// node containing that
		// character can be found.
		for (int i = 0; i < s.length(); i++) {
			foundChild = false;
			for (Node j : temp.children) {
				if (s.charAt(i) == j.data) {
					// If one is found, we move down the tree and set our
					// tracker boolean to true.
					temp = j;
					foundChild = true;
				}
			}

			// However, if our tracker boolean is still false by this point,
			// then we know the character, and by extension the word, is not in the trie.
			if (foundChild == false) {
				return false;
			}
		}
		// If we are able to reach this statement, then all that remains is to
		// check if the
		// node we ended on is shaded. If so, then the word is valid.
		return temp.isShaded;
	}

	// This is a basic addFile method to read in the word list given in the
	// assignment.
	public void addFile(String filename) throws FileNotFoundException {
		try {
			Scanner s = new Scanner(new File(filename));

			ArrayList<String> words = new ArrayList<>();
			while (s.hasNext()) {
				words.add(s.next());
			}

			s.close();

			for (String i : words) {
				this.add(i);
			}

		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		}
	}

	// Autocomplete, assisted by the recursiveTrieSearch method, takes the
	// string argument
	// and completes it with all of the possible valid words.
	public Set<String> autocomplete(String s) {
		Set<String> result = new HashSet<>();
		Node temp = root;
		int charCount = 0;

		// This first nested loop is to check if the string s is in the trie.
		// If not, then an empty set is immediately returned.
		for (int i = 0; i < s.length(); i++) {
			for (Node j : temp.children) {
				if (s.charAt(i) == j.data) {
					temp = j;
					charCount++;
				}
			}
		}

		if (charCount < s.length()) {
			return result;
		}

		// Now, a curString variable is created to add complete words to the
		// string if that node
		// is shaded.
		String curString = s;

		if (temp.isShaded) {
			result.add(curString);
		}

		// If there is more to be done, the recursiveTrieSearch is called.
		recursiveTrieSearch(result, curString, temp);

		return result;
	}

	// recursive call used in autocomplete() method
	// This method continues where the previous one left off, continuing down
	// the trie.
	// If shaded nodes are found, they add the string argument plus all
	// encountered letters to our set.
	public void recursiveTrieSearch(Set<String> a, String s, Node i) {
		for (Node j : i.children) {
			// If we come to the end of the trie, which means children will be
			// empty, then we check if that node is shaded.
			// If so, that word is added.
			if (i.children.size() == 0) {
				if (j.isShaded) {
					a.add(s + j.data);
				}
			} else {
				if (j.isShaded) {
					a.add(s + j.data);
				}
				// Here is where we recursively call, adding the previously
				// explored letter to the trie.
				recursiveTrieSearch(a, s + j.data, j);
			}
		}
	}

	// CloseMatches with the assistance of CloseMatchRecurse takes a string and
	// checks if it can be
	// made into a string in the trie with one change, either insertion,
	// deletion, or replacement.
	public Set<String> closeMatches(String s) {
		Set<String> result = new HashSet<>();
		// In the event that s is already a string that can be made in the trie,
		// it is added.
		if (this.contains(s)) {
			result.add(s);
		}
		closeMatchRecurse(result, s, this.root);

		return result;
	}

	public void closeMatchRecurse(Set<String> a, String s, Node i) {
		// In order to ensure only one degree of error is corrected, a boolean
		// is created.
		boolean oneEdit = false;

		// Finally, The children of the current node (i) are iterated through to
		// check for solutions.
		for (Node j : i.children) {
			// Adds a character to the beginning
			if (this.contains(j.data + s)) {
				a.add(j.data + s);
				oneEdit = true;
			}
			// Replaces a character at the beginning
			if (this.contains(j.data + s.substring(1, s.length()))) {
				a.add(j.data + s.substring(1, s.length()));
				oneEdit = true;
			}
			// Removes a character from the beginning
			if (this.contains(s.substring(1, s.length()))) {
				a.add(s.substring(1, s.length()));
				oneEdit = true;
			}
			// Removes a character from the end
			if (this.contains(s.substring(0, s.length() - 1))) {
				a.add(s.substring(0, s.length() - 1));
				oneEdit = true;
			}
			// Adds a character to the end
			if (this.contains(s + j.data)) {
				a.add(s + j.data);
				oneEdit = true;
			}
			// Replaces a character at the end
			if (this.contains(s.substring(0, s.length() - 1) + j.data)) {
				a.add(s.substring(0, s.length() - 1) + j.data);
				oneEdit = true;
			}
			// For the middle of the word, a new iterative loop is needed.
			for (int n = 0; n < s.length(); n++) {
				// First, we check for replacements for internal letters by
				// saving the letters before and after what we seek to replace.
				// Then, by creating a substring of one character, we ensure
				// that replace ONLY replaces it with a character in node j.
				if (this.contains(s.substring(0, n)
						+ s.substring(n, n + 1).replace(s.charAt(n), j.data)
						+ s.substring(n + 1))) {
					a.add(s.substring(0, n)
							+ s.substring(n, n + 1)
									.replace(s.charAt(n), j.data)
							+ s.substring(n + 1));
					oneEdit = true;
				}
				// A similar process is repeated here without replacing and
				// simply adding a new letter from j.
				if (this.contains(s.substring(0, n) + j.data + (s.substring(n)))) {
					a.add(s.substring(0, n) + j.data + (s.substring(n)));
					oneEdit = true;
				}
				// Finally, we take a letter out of the string entirely and
				// check if it makes a string that is in the trie.
				if (this.contains(s.substring(0, n) + s.substring(n + 1))) {
					a.add(s.substring(0, n) + s.substring(n + 1));
					oneEdit = true;
				}
			}

		}
		// This chain of checks insures that the length of the string never
		// drops to 0,
		// and that the above process is completed for every letter. If oneEdit
		// is false,
		// it means that no edits have been made for this word yet, and it must
		// be run through again.
		if (s.length() > 1) {
			for (Node j : i.children) {
				if (j.data == s.charAt(1) && !oneEdit) {
					closeMatchRecurse(a, s.substring(1), j);
				}
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		TrieSpellChecker testTrie = new TrieSpellChecker();

		// testing of addFile and contains methods
		testTrie.addFile("wordlist_English.txt");
		// These four statements test out the contain method, including
		// mispellings.
		System.out.println(testTrie.contains("abandon"));
		System.out.println(testTrie.contains("abandonment"));
		System.out.println(testTrie.contains("abandona"));
		System.out.println(testTrie.contains("inaccessiibility"));

		// Finally, we test out the autocomplete method and closeMatches method.
		// The accuracy at which these methods perform are...
		System.out.println(testTrie.autocomplete("legend"));
		// Wait for it...
		System.out.println(testTrie.closeMatches("ary"));
	}
}
