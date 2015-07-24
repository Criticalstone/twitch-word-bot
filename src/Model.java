public class Model {
  private final Map<String, long> wordList;

  public Model() {
    wordList = new HashMap<>();
  }

  public void addWord(String word) {
    if (wordList.containtKey(word)) {
      long count = wordList.get(word);
      wordList.put(word, ++count);
    } else {
      wordList.put(word, 1);
    }
  }

  public Map<String, long> getWordList() {
    return this.wordList();
  }

  public long getCountForWord(String word) {
    if (wordList.containsKey(word)) {
      return wordList.get(word);
    } else {
      return 0;
     }
  }

  public void addSentence(String sentence) {
    String[] words = sentence.split(' ');
    if (words.length > 0) {
      for (String word : words) {
        addWord(word);
      }
    }
  }
}
