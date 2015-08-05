package twiddlerbot;

import org.jibble.pircbot.IrcException;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Kevin Hoogendijk
 * @since 2015-07-24
 */
public class Main{

    private Twiddler twiddler;
    private Model model;
    private GeneralSettings settings;

    public Main() {
        this.model = new Model();
        this.twiddler = new Twiddler(model);
        this.settings = GeneralSettings.getInstance();

        try {
            //twiddler.connect("irc.twitch.tv", 6667, "oauth:ickib8fp0msgt3le4zca7vxg31825j");
            //twiddler.connect("irc.freenode.net");
            twiddler.connect(settings.getHostname(), settings.getPortNumber(), settings.getPassword());
        }catch(IOException | IrcException e){
            System.out.println(e.getMessage());
        }

        twiddler.joinChannel("#twiddlertest");

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
        if (input.equals("dc")) {
            twiddler.disconnect();
        } else if (input.startsWith("sendmessage")){
            String[] inputArray = input.split(" ", 2);
            if(inputArray.length > 1)
                twiddler.sendMessage("#hej", inputArray[1]);
        } else if (input.equals("stats")){
            System.out.println(model.getSortedStatsFormatted());
        } else if (input.equals("muw")){
            for(String string: model.getMostCountedWords()){
                System.out.println(string + " : " + model.getCountForWord(string));
            }
        } else if (input.startsWith("count")){
            String[] inputArray = input.split(" ", 2);
            if(inputArray.length > 1)
                System.out.println(inputArray[1] + " : " + model.getCountForWord(inputArray[1]));
        } else if (input.equals("tt")){
            String stats = model.getSortedStatsFormatted();
            String[] splitStats = stats.split("\\n");
            String[] ttStats = new String[10];
            for (int i = 1; i < (splitStats.length < 11 ? splitStats.length : 10); i++){
                ttStats[i - 1] = splitStats[splitStats.length - i];
            }
            for(String string: ttStats)
                System.out.println(string);

        } else if(input.equals("load")) {
            try {
                model.load("stats.txt");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}

//TODO: Save event stats
//TODO: Sort stats on value

