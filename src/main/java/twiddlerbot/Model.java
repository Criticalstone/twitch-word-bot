package twiddlerbot;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Alexander Håkansson, Kevin Hoogendijk
 * @since 2015-07-24
 */
public class Model {
    private final Map<String, Long> wordList;
    private final Set<OnModelDataChangedListener> listeners;

    public Model() {
        wordList = new HashMap<>();
        listeners = new HashSet<>();
    }

    public void addWord(String word) {
        addWord(word, 1);
    }

    public void addWord(String word, long count){
        long oldCount = getCountForWord(word);
        word = word.toLowerCase();
        if (wordList.containsKey(word)){
            long tempCount = wordList.get(word);
            wordList.put(word, tempCount+count);
        } else {
            wordList.put(word, count);
        }
        notifyListeners(word, getCountForWord(word), oldCount);
    }

    public Map<String, Long> getWordList() {
        return this.wordList;
    }

    public Map<String, Long> getEventWordList() {return this.wordList; }

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
            PrintWriter globalWriter = new PrintWriter(Constants.GLOBAL_STATS_FILE);
            globalWriter.print(getStatsFormatted(wordList));
            globalWriter.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void load(String path) throws IOException{
        Path filepath = FileSystems.getDefault().getPath(path);
        ArrayList<String> Stringlist = (ArrayList)Files.readAllLines(filepath);
        for (String line : Stringlist){
            addWord(line.substring(0, line.indexOf(" : ")), Integer.parseInt(line.substring(line.indexOf(" : ") + 3)));
        }
    }

    public TreeMap<String, Long> getSortedStats(){
        ValueComparator vc = new ValueComparator(wordList);
        TreeMap<String, Long> sortedStats = new TreeMap<>(vc);
        sortedStats.putAll(wordList);
        return sortedStats;
    }

    public String getSortedStatsFormatted(){
        String stats = "";
        for(String string: getSortedStats().descendingKeySet()){
            stats += string + " : " + getCountForWord(string) + "\n";
        }
        return stats;
    }

    public String getStatsFormatted(Map<String, Long> wordList){
        String stats = "";
        for(String string: wordList.keySet()){
            stats += string + " : " + getCountForWord(string) + "\n";
        }
        return stats;
    }

    private class ValueComparator implements Comparator<String> {
        Map<String, Long> base;
        public ValueComparator(Map<String, Long> base) {
            this.base = base;
        }

        @Override
        public int compare(String a, String b) {
            if(base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
