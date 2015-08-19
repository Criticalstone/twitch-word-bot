package twiddlerbot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *  @author Kevin Hoogendijk
 *  @since 2015-08-19
 */
public class Event implements Model.OnModelDataChangedListener {
    private Model model;
    private Map<String, Long> eventWordList;
    private String eventName;

    public Event(Model model, String eventName){
        this.model = model;
        model.addListener(this);
        this.eventWordList = new HashMap<>();
        this.eventName = eventName;
    }

    @Override
    public void onModelDataChanged(String word, long newCount, long oldCount) {
        addWord(word, newCount-oldCount);
    }

    public String getEventName(){
        return this.eventName;
    }

    public void addWord(String word, long count){
        word = word.toLowerCase();
        if (eventWordList.containsKey(word)){
            long oldCount = eventWordList.get(word);
            eventWordList.put(word, oldCount+count);
        } else {
            eventWordList.put(word, count);
        }
    }

    public void save() {
        try {
            PrintWriter writer = new PrintWriter(eventName + ".txt");
            writer.print(model.getStatsFormatted(eventWordList));
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
