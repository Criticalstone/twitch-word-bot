import org.jibble.pircbot.IrcException;

import java.io.IOException;

/**
 * Created by kritt on 7/24/15.
 */
public class Main {
    public static void main(String[] args) {
        Twiddler twiddler = new Twiddler();

        try {
            twiddler.connect("irc.chalmers.it", 6667);
        }catch(IOException | IrcException e){
            System.out.println(e.getMessage());
        }

        twiddler.joinChannel("#android");
        twiddler.sendRawLine("Hello, I'm Twiddler. Nice to meet you");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        twiddler.disconnect();

    }
}
