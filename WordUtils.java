
/**
 *	Provides utilities for word games:
 *	1. finds all words in the dictionary that match a list of letters
 *	2. prints an array of words to the screen in tabular format
 *	3. finds the word from an array of words with the highest score
 *	4. calculates the score of a word according to a table
 *
 *	Uses the FileUtils and Prompt classes.
 *	
 *	@author Jason He
 *	@since	10/17/2025
 */
 
public class WordUtils
{
	private String[] words;		// the dictionary of words
	private int numWords;		// number of words loaded
	
	// File containing dictionary of almost 100,000 words.
	private final String WORD_FILE = "wordList.txt";
	
	/* Constructor */
	public WordUtils() { 
		words = new String[100000]; // Large array to hold dictionary
		numWords = 0;
	}
	
	/**	Load all of the dictionary from a file into words array. */
	private void loadWords () { 
		java.util.Scanner input = FileUtils.openToRead(WORD_FILE);
		numWords = 0;
		
		while (input.hasNext() && numWords < words.length) {
			words[numWords] = input.next();
			numWords++;
		}
		
		input.close();
	}
	
	/**	Find all words that can be formed by a list of letters.
	 *  @param letters	string containing list of letters
	 *  @return			array of strings with all words found.
	 */
	public String [] findAllWords (String letters)
	{		
		java.util.ArrayList<String> foundWords = new java.util.ArrayList<String>();
		
		for (int i = 0; i < numWords; i++) {
			if (isWordMatch(words[i], letters)) {
				foundWords.add(words[i]);
			}
		}
		
		// Convert ArrayList to array
		String[] result = new String[foundWords.size()];
		for (int i = 0; i < foundWords.size(); i++) {
			result[i] = foundWords.get(i);
		}
		
		return result;
	}
	
	/**	Check if a word can be formed using the given letters
	 *  @param word		the word to test
	 *  @param letters	the available letters
	 *  @return			true if the word can be formed, false otherwise
	 */
	private boolean isWordMatch(String word, String letters) {
		String tempLetters = letters.toLowerCase();
		String lowerWord = word.toLowerCase();
		
		for (int i = 0; i < lowerWord.length(); i++) {
			char c = lowerWord.charAt(i);
			int index = tempLetters.indexOf(c);
			if (index == -1) {
				return false;
			}
			// Remove the used letter
			tempLetters = tempLetters.substring(0, index) + tempLetters.substring(index + 1);
		}
		return true;
	}
	
	/**	Print the words found to the screen.
	 *  @param words	array containing the words to be printed
	 */
	public void printWords (String [] wordList) { 
		int count = 0;
		for (String word : wordList) {
			System.out.printf("%-8s", word);
			count++;
			if (count % 10 == 0) {
				System.out.println();
			}
		}
		if (count % 10 != 0) {
			System.out.println();
		}
	}
	
	/**	Finds the highest scoring word according to a score table.
	 *
	 *  @param word  		An array of words to check
	 *  @param scoreTable	An array of 26 integer scores in letter order
	 *  @return   			The word with the highest score
	 */
	public String bestWord (String [] wordList, int [] scoreTable)
	{
		if (wordList.length == 0) {
			return "";
		}
		
		String bestWord = wordList[0];
		int highestScore = getScore(bestWord, scoreTable);
		
		for (String word : wordList) {
			int score = getScore(word, scoreTable);
			if (score > highestScore) {
				highestScore = score;
				bestWord = word;
			}
		}
		
		return bestWord;
	}
	
	/**	Calculates the score of one word according to a score table.
	 *
	 *  @param word			The word to score
	 *  @param scoreTable	An array of 26 integer scores in letter order
	 *  @return				The integer score of the word
	 */
	public int getScore (String word, int [] scoreTable)
	{
		int score = 0;
		String lowerWord = word.toLowerCase();
		
		for (int i = 0; i < lowerWord.length(); i++) {
			char c = lowerWord.charAt(i);
			if (c >= 'a' && c <= 'z') {
				score += scoreTable[c - 'a'];
			}
		}
		
		return score;
	}
	
	/***************************************************************/
	/************************** Testing ****************************/
	/***************************************************************/
	public static void main (String [] args)
	{
		WordUtils wu = new WordUtils();
		wu.run();
	}
	
	public void run() {
		String letters = Prompt.getString("Please enter a list of letters, from 3 to 12 letters long, without spaces");
		loadWords();
		String [] word = findAllWords(letters);
		System.out.println();
		printWords(word);
		
		// Score table in alphabetic order according to Scrabble
		int [] scoreTable = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
		String best = bestWord(word,scoreTable);
		System.out.println("\nHighest scoring word: " + best + "\nScore = " 
							+ getScore(best, scoreTable) + "\n");
	}
}
