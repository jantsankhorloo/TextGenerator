import java.util.Random;

/**
 * CS 180 - Dynamic Generation Project
 * 
 * Prefix class that represents prefixes used in a Dynamic Text Generator. A
 * prefix can have a fixed but arbitrary number of context words.
 *
 * @author Jantsankhorloo Amgalan <jamgalan@purdue.edu>
 *
 */
public class Prefix {

	/**
	 * The array of prefix strings that denote the start of a sentence is an
	 * array of empty strings. This method must be called and the array must be
	 * initialized every time the program is trained on a new file so that the
	 * array will have the correct length (i.e. correct number of elements).
	 * <P>
	 * If this method is not called every time a new file is trained, the
	 * program may inexplicably fail (especially if the length of the prefix has
	 * been changed).
	 */
	
	public static int numPref = 3; //value for prefix
	private int numSuff;
	private static String[] prefixStart;
	private String[] prefixes;	
	private String[] suffixes;
	
	public static void initializeSentenceStartArray() {
		prefixStart = new String[numPref];
		
		for (int i = 0; i < numPref; i++)
			prefixStart[i] = "";
	}

	/**
	 * Returns a String[] whose contents are used to retrieve the
	 * Start-of-Sentence Prefix object.
	 * <P>
	 * This Start-of-Sentence Prefix object is special in that is used to
	 * determine the first word in a dynamically generated sentence.
	 * 
	 * @return A <b>copy</b> of the prefix strings array initialized by
	 *         {@link #initializeSentenceStartArray()}
	 */
	public static String[] getStartOfSentencePrefixes() {
		return prefixStart;
	}

	/**
	 * Constructor that takes an array of prefix strings as an argument
	 * 
	 * @param prefixStrings
	 *            The array of prefix strings
	 */
	public Prefix(String[] prefixStrings) {
		this.prefixes = prefixStrings;
		this.numSuff = 0;
		this.suffixes = new String[1]; //currently assigned to an element of null
	}

	/**
	 * Returns the number of suffixes that this Prefix object contains
	 * 
	 * @return The number of suffixes of this prefix object
	 */
	public int getNumSuffixes() {
		return this.numSuff;
	}

	/**
	 * Returns the number of prefixes that this Prefix object contains
	 * 
	 * @return The number of of prefix strings of this prefix object
	 */
	public int getNumPrefixes() {
		return this.prefixes.length;
	}

	/**
	 * Returns the prefix string at a specified index.
	 * 
	 * @param index
	 *            The location of the desired prefix string
	 * @return The prefix string at this given location
	 */
	public String getPrefixString(int index) {
		return this.prefixes[index];
	}

	/**
	 * Returns the suffix string at a specified index
	 * 
	 * @param index
	 *            The location of the desired suffix string
	 * @return The suffix string at this given location
	 */
	public String getSuffixString(int index) {
		return suffixes[index];
	}

	/**
	 * Selects a random suffix from the array of suffixes. This function is
	 * integral to the sentence generation stage. It should use Math.random() to
	 * select a valid index for the array of suffixes, and then return the
	 * string at that index.
	 * <P>
	 * Warning: If the suffix array is empty (i.e. suffixes.length == 0), this
	 * method will throw an {@link ArrayIndexOutOfBoundsException}. Therefore,
	 * before calling this method, you may want to check the number of suffixes
	 * first.
	 * 
	 * @return - A random suffix string (from the suffix strings array)
	 */
	public String getRandomSuffix() {
		if (suffixes.length == 0)
			throw new ArrayIndexOutOfBoundsException();
		
		Random random = new Random();		
		return this.suffixes[random.nextInt(suffixes.length - 1)];
	}

	/**
	 * Adds a suffix string to the array of all possible suffixes that appear
	 * after this prefix. This method allows for multiple copies of the same
	 * string to be added to the array.
	 * 
	 * @param str
	 *            The string suffix (word appearing directly after this prefix)
	 */
	public void addSuffix(String str) {
		
		this.suffixes[numSuff++] = str;
		if (numSuff >= this.suffixes.length) {
			String[] tempSuffixes = new String[this.suffixes.length + 1];
			for (int i = 0; i < this.suffixes.length; i++)
				tempSuffixes[i] = this.suffixes[i];
			this.suffixes = tempSuffixes;
		}
	}

	/**
	 * Determines equality among Prefix objects. Two Prefix objects are
	 * considered equal if they both have the exact same string prefixes in the
	 * same order.
	 * 
	 * @param obj
	 *            Object to determine equality against
	 */
	public boolean equals(Object obj) {
		Prefix p = (Prefix) obj;
		
		if (p.toString().equals(null) || p.toString().equals(""))
			return false;
		//change it to else if to exit the code if above condition satisfies
		else if (this.toString().equals(p.toString()))
			return true;
		else
			return false;
	}

	/**
	 * The string form of a prefix object is its list of prefixes converted to a
	 * whitespace delimited string
	 */
	public String toString() {
		String prefixInString = "";
		for (int i = 0; i < this.prefixes.length; i++)
			prefixInString = prefixInString + this.prefixes[i] + " ";
		return prefixInString;
	}
}
