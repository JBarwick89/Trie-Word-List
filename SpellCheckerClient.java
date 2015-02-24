import java.io.FileNotFoundException;

//Josh Barwick
//This code times the adding and retrieval of words using both types of data structures and compares the runtime for both.

public class SpellCheckerClient {
	public static void main(String[] args) throws FileNotFoundException {
		double start, finish;
		String file = "wordlist_english.txt";

		TrieSpellChecker trieTest = new TrieSpellChecker();
		BSTSpellChecker BSTTest = new BSTSpellChecker();

		System.out.println("\nrunning balanced add method for BSTSpellChecker....");
		start = System.nanoTime();
		BSTTest.balancedAddFile(file);
		finish = System.nanoTime();
		System.out.println("runtime for addFile with BSTSpellChecker: " + ((finish - start) / 1000000000) + " seconds");

		System.out.println("\n\nrunning addFile for TrieSpellChecker...");
		start = System.nanoTime();
		trieTest.addFile(file);
		finish = System.nanoTime();
		System.out.println("runtime for addFile with TrieSpellChecker: " + ((finish - start) / 1000000000) + " seconds");

		System.out.println("\n\nTesting retrieval with BSTSpellChecker...\nword = \"roundabout\"");
		start = System.nanoTime();
		BSTTest.contains("roundabout");
		finish = System.nanoTime();
		System.out.println("runtime for contains() with BSTSpellChecker: " + ((finish - start) / 1000000000) + " seconds");

		System.out.println("\n\nTesting retrieval with TrieSpellChecker...\nword = \"roundabout\"");
		start = System.nanoTime();
		trieTest.contains("roundabout");
		finish = System.nanoTime();
		System.out.println("runtime for contains with TrieSpellChecker: " + ((finish - start) / 1000000000) + " seconds");

		System.out.println("\n\nTesting retrieval with BSTSpellChecker...\nword = \"lebowski\"");
		start = System.nanoTime();
		BSTTest.contains("lebowski");
		finish = System.nanoTime();
		System.out.println("runtime for contains with BSTSpellChecker with word not in list: " + ((finish - start) / 1000000000) + " seconds");

		System.out.println("\n\nTesting retrieval with TrieSpellChecker...\nword = \"lebowski\"");
		start = System.nanoTime();
		trieTest.contains("lebowski");
		finish = System.nanoTime();
		System.out.println("runtime for contains with TrieSpellChecker with word not in list: " + ((finish - start) / 1000000000) + " seconds");

	}
}
