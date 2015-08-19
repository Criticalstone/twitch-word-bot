package twiddlerbot;

import org.jibble.pircbot.PircBot;

import java.util.Random;

/**
 * @author Kevin Hoogendijk, Alexander Håkansson
 * @since 2015-07-24
 */
public class Twiddler extends PircBot{
    private Model model;
    private Random rand;
    public Twiddler(Model model){
        this.setName("GenericRussian3");
        this.model = model;
        this.rand = new Random();
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        
        if (WhitelistSetting.getInstance().canCommand(sender)) {
            if(message.equals("bark")){
                randomDelay();
                sendMessage(channel, "Hi " + sender + "!");
            } else if (message.equals("dc")){
                disconnect();
            } else if (message.startsWith(WhitelistSetting.WHITELIST_PREFIX)) {
                WhitelistSetting.getInstance().handle(message);
            }
        } else {
            System.out.println(sender + " tried to run '" + message + "' but was not granted permission");
        }

        model.addSentence(message);

    }

    public void randomDelay(){
        try{
            Thread.sleep((long)(rand.nextDouble()*5000));
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onDisconnect() {
        super.onDisconnect();
        model.save();
        System.exit(0);
    }
}
