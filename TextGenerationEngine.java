import java.io.File;
import java.util.Scanner;

/**
 * CS 180 - Dynamic Generation Project
 * 
 * This class prompts the user for an action and can dynamically generate
 * sentences if the program has already been trained.
 * 
 * @author (Your Name) <(YourEmail@purdue.edu)>
 * 
 * @lab (Your Lab Section)
 * 
 * @version (Today's Date)
 *
 */
public class TextGenerationEngine {
    private static String[] terminators = { ".", "!", "?" };
    private static String[] trainedTexts = new String[8];
    private static int numTextsTrained = 0;
    private static StringArrayMap map = new StringArrayMap();
    private static int numSentences = 1;
    
    /**
     * Adds a file to the list of files that the program has been trained on
     * 
     * @param filename
     *            Name of the file to add
     */
    public static void addTrainedTexts(String filename) {
        trainedTexts[numTextsTrained++] = filename;
        if (numTextsTrained >= trainedTexts.length) {
            String[] temp = new String[2 * trainedTexts.length];
            for (int i = 0; i < trainedTexts.length; ++i)
                temp[i] = trainedTexts[i];
            trainedTexts = temp;
        }
    }
    
    /**
     * Determines if the program has been trained on a file
     * 
     * @param filename
     *            The name of the file to check
     * @return <code>true</code> if the model has been trained on that file,
     *         <code>false </code>otherwise
     */
    public static boolean haveTrainedText(String filename) {
        for (int i = 0; i < numTextsTrained; ++i) {
            if (filename.equals(trainedTexts[i]))
            	return true;
        }
        return false;
    }
    
    /**
     * A method used to prompt the user for desired action
     */
    public static void promptUser() {
        System.out.println("0 - Terminate Program");
        System.out.println("1 - Generate Sentence");
        System.out.println("2 - Train Program on File");
        System.out.println("3 - Change Number of Words in Prefix");
        System.out.println("4 - Number of Sentences to Generate");
        System.out.print("Action: ");
    }
    
    /**
     * The main method used to run the engine
     */
    public static void main(String[] args) {
        // scanner opened here, closed later when the program is selected to end
        Scanner in = new Scanner(System.in);
        
        while (true) {
            int decision = -1;
            
            // Prompt user input/action
            do {
                promptUser();
                try {
                    decision = in.nextInt();
                } catch (Exception e) {
                    decision = -1;
                }
                in.nextLine();
            } while (decision < 0);
            
            switch (decision) {
                case 0:
                    System.out.printf("\nProgram Ending\n");
                    in.close();
                    return;
                    
                case 1:
                    if (numTextsTrained < 1) {
                    	System.out.printf("Program has not been trained yet\n\n");
                    	break;
                    }	
                    
                    System.out.printf("\nDynamically Generated Text\n\n");
                    for (int i = 0; i < numSentences; ++i) {
                        String sentence = generateSentence(map);
                        System.out.printf("%s\n\n", sentence);
                    }
                    break;
                    
                case 2:
                    String filename = null;
                    do {
                        System.out.print("Enter file name ('0' for menu): ");
                        filename = in.next();
                        in.nextLine();
                        
                        // Allow user to return to main menu
                        if (filename.equals("0"))
                            break;
                        
                        // Check if program has already been trained on this text
                        if (haveTrainedText(filename)) {
                            System.out.printf("Program has already been trained on this text\n\n");
                            filename = null;
                            continue;
                        }
                        
                        // Check that the file is valid
                        File check = new File(filename);
                        if (!check.isFile()) {
                            System.out.printf("Invalid file name\n\n");
                            filename = null;
                            continue;
                        }
                        
                        PrefixGenerator.trainPrefixMap(map, filename);
                        
                    } while (filename == null);
                    
                    addTrainedTexts(filename);
                    System.out.println();
                    break;
                    
                case 3:
                    int length = -1;
                    do {
                        System.out.print("Number of Words in Prefix: ");
                        length = in.nextInt();
                        if (length <= 0)
                            System.out.println("Invalid input");
                        in.nextLine();
                    } while (length <= 0);
                    
                    map = retrain(length);
                    
                    System.out.println("All texts re-trained\n");
                    break;
                    
                case 4:
                    int num = -1;
                    do {
                        System.out.print("Num. of Sentences: ");
                        num = in.nextInt();
                        if (num < 0)
                            System.out.println("Invalid input");
                    } while (num < 0);
                    numSentences = num;
                    System.out.println();
                    break;
                    
                default:
                    System.out.printf("Invalid program action\n\n");
            }
        }
    }
    
    /**
     * Determines if a given character is punctuation
     * 
     * @param c
     *            The character to check
     * @return <code>true</code> if it is a punctuation character,
     *         <code>false</code> otherwise
     */
    public static boolean isPunctuation(char c) {
        return c == '.' || c == ',' || c == '?' || c == '!' || c == ';'
            || c == ':' || c == '"' || c == '(' || c == ')' || c == '\'';
    }
    
    /**
     * Determines if the specified string is at the end of a sentence. A word is
     * defined to be at the end of a sentence if a string from the "terminators"
     * array occurs at the end of the word before any non-punctuation characters
     * occur. The terminator string doesn't need to be at the very end of the
     * string for it to signal the end of the sentence.
     * <P>
     * Examples:
     * <ul>
     * <li>"..... goodbye." - end of sentence</li>
     * <li>".....  good? ....." - end of sentence</li>
     * <li>"..... 1.5 ....." - NOT end of sentence</li>
     * </ul>
     * 
     * @param suffix
     *            The word to check
     * @return <code>true</code> if word occurs at the end of a sentence,
     *         <code>false</code> otherwise
     */
    
    // helper method: check if terminator is at the end
    public static boolean endTerminator(String suffix) {
        for (int i = 0; i < terminators.length; i++) {
            if (Character.toString(suffix.charAt(suffix.length() - 1)).equals(terminators[i])) {
                return true;
            }
        }       
        return false;
    }
    
    //helper method: check if the passing argument is a terminator
    public static boolean isTerminator(char c) {
        for (int i = 0; i < terminators.length; i++) {
            if (c == terminators[i].charAt(0)) {
                return true;
            }
        }
        return false;
    }
    
    // helper method: Check if contains terminator
    public static boolean containsTerminator (String suffix) {
        for (int i = 0; i < terminators.length; i++) {
            if (suffix.indexOf(terminators[i]) == -1) {
                return false;
            }
        }      
        return true;
    }
    
    public static boolean shouldTerminate(String suffix) { 
        suffix = suffix.trim();
        
        if (suffix == null || suffix.equals("") || suffix.length() == 1) {
            return false;
        }
        
        for (int i = suffix.length() - 1; i >= 0; i--) {
        	if (isTerminator(suffix.charAt(i)))
        		return true;
        	if (!isPunctuation(suffix.charAt(i)))
        		return false;
        }      
        return false;
    }
    
    /**
     * Using a StringArrayMap of String[]'s to Prefix objects, dynamically
     * generate a sentence by selecting a random suffix of the current prefix.
     * 
     * @return The generated sentence
     */
    public static String generateSentence(StringArrayMap map) {
        Prefix.initializeSentenceStartArray();
        String word = ""; 
        String sentence = "";
        String[] prefixWords = Prefix.getStartOfSentencePrefixes();
        
        while (true)  {
        	
        	if (map.getPrefix(prefixWords) == null) {
        		break;
        	}
        	
        	else
        		word = map.getPrefix(prefixWords).getRandomSuffix().trim();
            
            String[] tempPrefix = new String[prefixWords.length];
            
            for (int i = 0; i < prefixWords.length - 1; i++) {
                tempPrefix[i] = prefixWords[i + 1]; 
            }   
            
            tempPrefix[prefixWords.length - 1] = word; 
            
            prefixWords = tempPrefix;
            
            if (word.equals("i")) {
            	word = "I";
            }
            
            if (isTerminator(word.charAt(word.length() - 1))) {
            	sentence = sentence + word;
            }
            
            else if (isPunctuation(word.charAt(word.length() - 1))) {
            	sentence = sentence + word + "\n";
            }
            
            else {
            	sentence = sentence + word + " ";
            }
            
            if (shouldTerminate(word))
                break;
        }
        
        sentence = Character.toString(sentence.charAt(0)).toUpperCase() + sentence.substring(1);
        return sentence;
    }
    
    /**
     * Make a new {@link StringArrayMap} resulting from training the model on
     * all of the files stored in the {@link #trainedTexts} array, but with the
     * specified number of words in a prefix
     * 
     * @param prefixLength
     *            Number defining the count of words in a prefix
     * 
     * @return The new {@link StringArrayMap} resulting from retraining
     */
    public static StringArrayMap retrain(int prefixLength) {
        
        Prefix.numPref = prefixLength;
        Prefix.initializeSentenceStartArray();
        StringArrayMap m = new StringArrayMap();
        
        for (int i = 0; i < numTextsTrained; i++) {
            PrefixGenerator.trainPrefixMap(m, trainedTexts[i]);
        }
        return m;
    }
}
