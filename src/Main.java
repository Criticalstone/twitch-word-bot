import org.jibble.pircbot.IrcException;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by kritt on 7/24/15.
 */
public class Main{

    private Twiddler twiddler;

    public Main() {
        Model model = new Model();
        this.twiddler = new Twiddler(model);

        try {
            //twiddler.connect("irc.twitch.tv", 6667, "oauth:ickib8fp0msgt3le4zca7vxg31825j");
            twiddler.connect("irc.freenode.net");
        }catch(IOException | IrcException e){
            System.out.println(e.getMessage());
        }

        twiddler.joinChannel("#hej");
        twiddler.sendMessage("#hej", "Hello! :)");

        getInput();
    }

    public static void main(String[] args) {
        new Main();
    }

    private void getInput() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (true) {
            System.out.println("Enter command");
            System.out.print("> ");
            input = scanner.nextLine();
            System.out.println(input);
            handleInput(input);
        }
    }

    private void handleInput(String input) {
        if (input.equals("disconnect")) {
            twiddler.disconnect1();
        }
        if (input.startsWith("sendmessage")){
            String[] inputArray = input.split(" ", 2);
            if(inputArray.length > 1)
                twiddler.sendMessage("#hej", inputArray[1]);
        }
    }
}
