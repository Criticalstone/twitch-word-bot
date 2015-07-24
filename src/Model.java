import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Model {
    private final Map<String, Long> wordList;
    private final Set<OnModelDataChangedListener> listeners;

    public Model() {
        wordList = new HashMap<>();
        listeners = new HashSet<>();
    }

    public void addWord(String word) {
        word = word.toLowerCase();
        if (wordList.containsKey(word)) {
            long count = wordList.get(word);
            wordList.put(word, ++count);
        } else {
            wordList.put(word, 1l);
        }
    }

    public Map<String, Long> getWordList() {
        return this.wordList;
    }

    public long getCountForWord(String word) {
        if (wordList.containsKey(word)) {
            return wordList.get(word);
        } else {
            return 0;
        }
    }

    public void addSentence(String sentence) {
        String[] words = sentence.split(" ");
        if (words.length > 0) {
            for (String word : words) {
                addWord(word);
            }
        }
    }


    public long getHighestCount() {
        List<Long> counts = new ArrayList<>(wordList.values());
        Collections.sort(counts);
        return counts.get(counts.size()-1);
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

    public boolean isListenerAdded(OnModelDataChangedListener listener, Map<String, Long> wordList) {
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

    public void save(){
        try {
            PrintWriter writer = new PrintWriter("stats.txt");
            writer.print(getStatsFormatted());
            writer.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public String getStatsFormatted(){
        String stats = "";
        for(String string: getWordList().keySet()){
            stats += string + " : " + getCountForWord(string) + "\n";
        }
        return stats;
    }
}
