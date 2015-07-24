import org.jibble.pircbot.PircBot;

import java.util.Random;

/**
 * Created by kritt on 7/24/15.
 */
public class Twiddler extends PircBot{
    private Model model;
    private Random rand;
    public Twiddler(Model model){
        this.setName("Twiddler123");
        this.model = model;
        this.rand = new Random();
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {

        if(message.equals("bark")){
            randomDelay();
            sendMessage(channel, "Hi " + sender + "!");
        }

        if(message.equals("dc")){
            disconnect();
        }

        model.addSentence(message);
        /*
        for(String string: model.getWordList().keySet()){
            System.out.println("#######################################################################");
            System.out.println(string + " : " + model.getCountForWord(string));
            System.out.println("#######################################################################");
        }*/
    }

    public void randomDelay(){
        try{
            Thread.sleep((long)(rand.nextDouble()*5000));
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    public void disconnect1(){
        disconnect();
    }
}
