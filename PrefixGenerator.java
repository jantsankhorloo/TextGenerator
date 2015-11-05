import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * CS 180 - Dynamic Generation Project
 * 
 * This class provides methods to train the Dynamic Text Generator program. This
 * class only provides static methods and should never be instantiated.
 * 
 * @author Jantsankhorloo Amgalan <jamgalan@purdue.edu>
 * 
 * @lab L05
 * 
 * @version (Today's Date)
 *
 */
public class PrefixGenerator {

	/**
	 * Using a given map of String[]'s to Prefix objects and the name of the
	 * training file, this method trains the program by associating Prefix
	 * objects with the suffix string (i.e. the word that appears directly after
	 * the current prefix). The resulting trained Prefix objects are stored in
	 * the given map.
	 * <P>
	 * It is valid that the map object can contain the results of prior training
	 * when this method is called. In fact, supporting this case is required to
	 * allow your program to be trained on multiple files.
	 * 
	 * @param map
	 *            The map of String[] to Prefix objects to which the resulting
	 *            mappings are appended
	 * @param filename
	 *            The name of the file on which to train the model
	 */
	public static void trainPrefixMap(StringArrayMap map, String filename) {
		// Open scanner on the file
		Scanner text;
		try {
			text = new Scanner(new File(filename));
		} catch (IOException io) {
			System.out.printf("File '%s' failed to open\n", filename);
			return;
		}

		// Assumes that the file has at least 1 word in it
		if (!text.hasNext()) {
			System.out.println("File is empty");
			text.close();
			return;
		}

		// TODO: Implement training algorithm
		
		Prefix.initializeSentenceStartArray();
		map.putPrefix(Prefix.getStartOfSentencePrefixes(), new Prefix(Prefix.getStartOfSentencePrefixes()));
		
		String word = "";
		String[] prefixWords = Prefix.getStartOfSentencePrefixes();
		
		while (text.hasNext()) {
		    word = text.next().toLowerCase().trim();
		    map.getPrefix(prefixWords).addSuffix(word);
		      
		    String[] tempPrefix = new String [prefixWords.length];
		      
		    for (int i = 0; i < prefixWords.length - 1; i++) {
		        tempPrefix[i] = prefixWords[i + 1]; 
		    }
		      
		    tempPrefix[prefixWords.length - 1] = word; 
		      
		    prefixWords = tempPrefix;
		      
		    if (TextGenerationEngine.shouldTerminate(word)) {
		        for (int i = 0; i < prefixWords.length; i++) {
		            prefixWords[i] = ""; 
		        }
		    }
		      
		    if (map.getPrefix(prefixWords) == null) {
		        map.putPrefix(prefixWords, new Prefix(prefixWords));
		    }	      		      
		} 
		  
		text.close();
	}
}
