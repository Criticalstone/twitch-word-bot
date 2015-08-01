import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
	
	private static final String WHITELIST_FILE = "whitelist.bin";
	
	private List<String> whitelist;
	
	private boolean enabled = true;
	
	private WhitelistSetting() {
		this.whitelist = loadState();
	}
	
	public static WhitelistSetting getInstance() {
		if (instance == null) {
			instance = new WhitelistSetting();
		}
		
		return instance;
	}	
	
	/**
	* Determines wheter a sender can send commands to the Twiddler bot or not.
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
	
	private List<String> loadState() {
		List<String> result;
		try {
			FileInputStream fin = new FileInputStream(WHITELIST_FILE);
			ObjectInputStream ois = new ObjectInputStream(fin);
			result = (List<String>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			result = new ArrayList<>();
		}

		return result;
	}
	
	private void saveState() {
		try {
			FileOutputStream fout = new FileOutputStream(WHITELIST_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(whitelist);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}