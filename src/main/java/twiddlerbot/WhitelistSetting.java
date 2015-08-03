package twiddlerbot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
* This class handles a whitelist of senders that can issue commands
* to the Twiddler bot. Settings will be saved over sessions and if
* no one is added to the whitelist then anyone can issue commands.
*
* This whitelist is global over all channels and servers.
*
* @author Alexander Håkansson
*/
public class WhitelistSetting {
	
	private static WhitelistSetting instance = null;
	
	public static final String WHITELIST_PREFIX = "whitelist";
	public static final String WHITELIST_FILE = "whitelist.txt";
    public static final String PROP_ENABLED = "whitelistEnabled";

	
	private List<String> whitelist;

    private Properties properties;
	
	private boolean enabled = true;
	
	private WhitelistSetting() {
        properties = new Properties(); // Must happen before loadState()
        loadState();
	}
	
	public static WhitelistSetting getInstance() {
		if (instance == null) {
			instance = new WhitelistSetting();
		}
		
		return instance;
	}	
	
	/**
	* Determines whether a sender can send commands to the Twiddler bot or not.
	*
	* @return Returns true if whitelist is empty or contains sender
	*/
	public boolean canCommand(String sender) {
		if (this.whitelist.size() == 0) {
			return true;
		} else {
			return this.whitelist.contains(sender);
		}
	}
	
	public void handle(String message) {
		if (!message.startsWith(WHITELIST_PREFIX)) {
			throw new IllegalArgumentException("Not a whitelist command");
		}
		
		String[] input = message.split(" ");
		
		if (input.length < 2) {
			throw new IllegalArgumentException("No command given");
		}
		
		String command = input[1];
		String arg;
		if (input.length >= 3) {
			arg = input[2];
		} else {
			arg = "";
		}
		
		handleClean(command, arg);
		
		saveState();
	}
	
	private void handleClean(String command, String arg) {
		switch (command) {
			case "add":
				addToWhiteList(arg);
				break;
			case "remove":
				removeFromWhiteList(arg);
				break;
			case "enable":
				this.enabled = true;
				break;
			case "disable":
				this.enabled = false;
				break;
			case "clear":
				this.whitelist.clear();
				break;
			default:
				throw new IllegalArgumentException("Not a valid command");
		}
	}
	
	private void addToWhiteList(String person) {
		person = person.trim();
		
		if (person.equals("")) {
			throw new IllegalArgumentException("No argument");
		}
		
		if (!whitelist.contains(person)) {
			whitelist.add(person);
		}
	}
	
	private void removeFromWhiteList(String person) {
		person = person.trim();
		
		if (person.equals("")) {
			throw new IllegalArgumentException("No argument");
		}
		
		if (whitelist.contains(person)) {
			whitelist.remove(person);
		}
	}
	
	private synchronized void loadState() {
        // Load whitelist
		List<String> result = new ArrayList<>();
		try (Scanner scanner = new Scanner(new FileInputStream(WHITELIST_FILE))) {
            scanner.forEachRemaining(result::add); // Le magic :D
		} catch (IOException e) {
            // Do nothing :)
        } finally {
            whitelist = result;
        }

        // Load whitelist enabled boolean
        try (FileInputStream fin = new FileInputStream(Constants.GLOBAL_PROP_FILE)) {
            properties.load(fin);
            String enabledString = properties.getProperty(PROP_ENABLED, "true");
            this.enabled = enabledString.equals("true");
        } catch (IOException e) {
            // Do nothing :)
        }
	}
	
	private synchronized void saveState() {
        // Save whitelist
		try (PrintWriter printer = new PrintWriter(new FileOutputStream(WHITELIST_FILE))){
           whitelist.forEach(printer::println); // Magic :D
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Save whitelist enabled boolean
        try (FileOutputStream fout = new FileOutputStream(Constants.GLOBAL_PROP_FILE)) {
            properties.setProperty(PROP_ENABLED, enabled ? "true" : "false");
            properties.store(fout, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}