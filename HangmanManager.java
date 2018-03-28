//Max Tran, CSE 143, April 28, 2016
//TA: Rebecca Yuen, Section: BL
import java.util.*;

/*This programs constructs a normal game of hangman which will
pick a word for the user to guess
*/
public class HangmanManager {
   private Set<String> words;
   private int remainingGuesses;
   private String result;
   private SortedSet<Character> alreadyGuesses;
   
   /*construct a hangman manager given list of words from a dictionary,
   length of the word, max number of guesses.
   Throw IllegalArgumentException when the length is < 1 or max is < 0
   Add all the possible words that has the same length to the set words
   */
   public HangmanManager(List<String> dictionary, int length, int max){
      if(length < 1 || max < 0){
         throw new IllegalArgumentException("ERROR: invalid word length/max guesses!");
      }
      result = "";
      remainingGuesses = max;
      alreadyGuesses = new TreeSet<Character>();
      for(int i = 0; i < length;i++){
         result += "- ";   
      }
      words = new TreeSet<String>();
      for(String word : dictionary){
         if (word.length() == length) {
            words.add(word);
         }
      }   
   }
   
   //return set of possible words that the program can use
   public Set<String> words(){
      return words;
   }
   
   //return the number of remaining guesses
   public int guessesLeft(){
      return remainingGuesses;
   }
   
   //return a sorted set of characters that the user already guessed
   public SortedSet<Character> guesses(){
      return alreadyGuesses;
   }
   
   /*return the pattern to display after the user guesses
   throw IllegalStateException if there are no possible words 
   that the computer can use */
   public String pattern(){
      if(words.size() == 0){
         throw new IllegalStateException("ERROR: No possible set of words");
      }
      return result.trim();
   }
   
   /*return how many times the character that the user guess appear on the pattern
   If its incorrect, the number of guess of the user decreases; otherwise, it's
   unchanged. 
   Throw IllegalStateException if the user doesnt have at least one guess or
   no possible words that the computer can use. Throw IllegalArgumentException if 
   the guessed character has already been guessed. */
   public int record(char guess){
      if(remainingGuesses < 1 || words.size() == 0){
         throw new IllegalStateException("ERROR");
      }
      if(words.size() > 0 && alreadyGuesses.contains(guess)){
         throw new IllegalArgumentException("You already guessed this!");
      }
      alreadyGuesses.add(guess);
      Map<String, Set<String>> patternList = wordsToPatternManager(guess);
      largestPattern(patternList);
      int num = 0;
      for (int i = 0; i < result.length(); i++) {
         if (result.charAt(i) == guess) {
            num++;
         }
      }
      if (num == 0) {
         remainingGuesses--;
      }
      return num;
   }
   
   //Out of patternList, pick a pattern with the largest number of element 
   private void largestPattern(Map<String, Set<String>> patternList) {
      int wordMaxSize = 0;
      for (String curPattern : patternList.keySet()) {
         Set<String> wordList = patternList.get(curPattern);
         if (wordMaxSize < wordList.size()) {
            wordMaxSize = wordList.size();
            result = curPattern;
            words = wordList;
         }
      }
   }
   
   //return groups of words that are categorized in each pattern in patternList
   private Map<String, Set<String>> wordsToPatternManager(char guess) {
      Map<String, Set<String>> patternList = new TreeMap<String, Set<String>>();
      for (String word : words) {
         String curPattern = addingWordstoPattern(word, guess);
         if (!patternList.containsKey(curPattern)) {
            patternList.put(curPattern, new TreeSet<String>());
         }
         Set<String> wordList = patternList.get(curPattern);
         wordList.add(word);
      }
      return patternList;
   }
   
   //return current pattern that includes the correct guess that the user made
   private String addingWordstoPattern(String word, char guess){
      String currentPattern = result;
      for (int i = 0; i < word.length(); i++) {
         if (word.charAt(i) == guess) {
            currentPattern = currentPattern.substring(0,2*i) + word.charAt(i) + currentPattern.substring(2*i+1);
         }
      }
      return currentPattern;
   }
}