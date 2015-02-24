import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// Implements a SpellChecker/Dictionary using a BSTSet data structure.

public class BSTSpellChecker {

	BinarySearchTree<String> wordList = new BinarySearchTree<>();
	
	public void add(String s){
		wordList.addIterative(s);
	}
	
	public boolean contains(String s){
		String result = wordList.findIterative(s); 
		return result != null;
	}
	
	// Reads in Strings from text file and adds them to BSTSpellChecker, but does so inefficiently if text file is alphabetized
	public void addFile(String filename) throws FileNotFoundException {
		try {
			Scanner s = new Scanner(new File(filename));
			
			ArrayList<String> words = new ArrayList<>();
			while (s.hasNext()){
				words.add(s.next());
			}
			
			s.close();
			
			for (String i : words){
				this.add(i);
			}
			
			
		} catch (FileNotFoundException e){
			throw new FileNotFoundException();
		}
	}
	

	// reads in Strings from text file and adds them to a BSTSpellChecker that is balanced
	public void balancedAddFile(String filename) throws FileNotFoundException{
		try {
			Scanner s = new Scanner(new File(filename));
			
			ArrayList<String> words = new ArrayList<>();
			while (s.hasNext() && words.size()<7){
				words.add(s.next());
			}
	
			recursiveAdd(words);
			s.close();
		} catch (FileNotFoundException e){
			throw new FileNotFoundException();
		}
	}
	
	
	// wrapper method for recursive add call
	public void recursiveAdd(ArrayList<String> s){
		recursiveAdd(0, s.size() - 1, s);
	}
	
	
	// recursive call that is used in the balancedAddFile method()
	// similar to recursive binary search method-->divide and conquer and add middle element of subarrary to BST
	public void recursiveAdd(int start, int end, ArrayList<String> s){
		if (start > end){
			return;
		} else {
			int middle = (end + start) / 2;
			add(s.get(middle));
			recursiveAdd(0, middle - 1, s);
			recursiveAdd(middle + 1, end, s);
		}
	}
	
	
	// toString for BSTSpellChecker
	// just uses the toString method for the underlying wordList BST
	public String toString(){
		return this.wordList.toString();
	}
	
	
	// main method used for testing of BSTSpellChecker class
	public static void main(String[] args) throws FileNotFoundException{
		BSTSpellChecker test = new BSTSpellChecker();
		
		 //testing of add method
		test.add("abate");
		test.add("abates");
		test.add("abated");
		System.out.println(test + "\n");
		test.add("unabated");
		test.add("abating");
		
		System.out.println(test);
		
		System.out.println(test.contains("abated"));
		System.out.println(test.contains("Roger"));
		
		// testing of addFile method
		test.addFile("wordlist_english.txt");
		System.out.println(test);
		
		
		// testing of balancedAddFileMethod
		test.balancedAddFile("wordlist_english.txt");
		System.out.println(test);
		
	}
}
