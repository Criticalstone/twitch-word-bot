import org.jibble.pircbot.IrcException;

import java.io.IOException;

/**
 * Created by kritt on 7/24/15.
 */
public class Main {
  
    private static Twiddler twiddler;  

    public static void main(String[] args) {
        twiddler = new Twiddler();

        try {
            twiddler.connect("irc.chalmers.it", 6667);
        }catch(IOException | IrcException e){
            System.out.println(e.getMessage());
        }

        getInput();
    }

    private static void getInput() {
      Scanner scanner = new Scanner(System.in);
      String input = "";
      while (true) {
        System.out.println("Enter command");
        System.out.print("> ");
        input = scanner.getLine();
        handleInput(input);
      }
    }

    private static void handleInput(String input) {
      
      if (input.equals("disconnect")) {
        twiddler.disconnect();
      }
    }
}
