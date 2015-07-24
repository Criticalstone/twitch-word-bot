public class Model {
  private final Map<String, long> wordList;
  private final Set<OnModelDataChangedListener> listeners;

  public Model() {
    wordList = new HashMap<>();
    listeners = new HashSet<>();
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

  public long getHighestCount() {
    Collection<Long> counts = new Collection(wordList.values());
    Collections.sort(counts);
    return counts.get(counts.size());
  }

  public String[] getMostCountedWords() {
    List<String> words = new ArrayList<>();
    long highestCount = getHighestCount();

    for (String word : wordList.keySet()) {
      if (wordList.get(word) == highestCount) {
        words.add(word);
      }
    }

    String[] wordsArray = new String[words.size()];
    for (int i = 0; i < words.size(); i++) {
      wordsArray[i] = words.get(i);
    }

    return wordsArray;
  }

  public void addListener(OnModelDataChangedListener listener) {
    if (listener != null) {
      listeners.add(listener);
    } else {
      throw new IllegalArgumentException("Listener can't be null");
    }
  }

  public void removeListener(OnModelDataChangedListener listener) {
    if (listeners.contains(listener)) {
      listeners.remove(listener);
    } else {
      throw new IllegalArgumentException("Listener is already removed");
    }
  }

  public isListenerAdded(OnModelDataChangedListener listener) {
    return listeners.contains(listener);
  }

  private void notifyListeners(String word, long newCount, long oldCount) {
    for (OnModelDataChangedListener l : listeners) {
      l.onModelDataChanged(word, newCount, oldCount);
    }
  }

  public interface OnModelDataChangedListener {
    void onModelDataChanged(String word, long newCount, long oldCount);
  } 
}
