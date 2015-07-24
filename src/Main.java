import org.jibble.pircbot.IrcException;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by kritt on 7/24/15.
 */
public class Main{

    private Twiddler twiddler;
    private Model model;

    public Main() {
        this.model = new Model();
        this.twiddler = new Twiddler(model);

        try {
            twiddler.connect("irc.twitch.tv", 6667, "oauth:ickib8fp0msgt3le4zca7vxg31825j");
            //twiddler.connect("irc.freenode.net");
        }catch(IOException | IrcException e){
            System.out.println(e.getMessage());
        }

        twiddler.joinChannel("#smitegame");

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
            twiddler.disconnect();
        }
        if (input.startsWith("sendmessage")){
            String[] inputArray = input.split(" ", 2);
            if(inputArray.length > 1)
                twiddler.sendMessage("#hej", inputArray[1]);
        }
        if (input.equals("stats")){
            System.out.println(model.getStatsFormatted());
        }
        if (input.equals("muw")){
            for(String string: model.getMostCountedWords()){
                System.out.println(string + " : " + model.getCountForWord(string));
            }
        }
        if (input.startsWith("count")){
            String[] inputArray = input.split(" ", 2);
            if(inputArray.length > 1)
                System.out.println(inputArray[1] + " : " + model.getCountForWord(inputArray[1]));
        }
    }
}

//TODO: Save event stats
//TODO: top ten words
//TODO: Sort stats on value

