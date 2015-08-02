package org.twiddlerbot.twiddlerbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Alexander Håkansson
 * @version 1.0.0
 * @since 2015-08-02
 */
public class GeneralSettings {
    private static GeneralSettings instance = null;

    public static final String PROP_HOST = "host";
    public static final String PROP_PORT = "port";
    public static final String PROP_PASS = "password";

    private final Properties properties;

    private String hostName;
    private int portNumber;
    private String password;

    private GeneralSettings() {
        properties = new Properties();
        loadSettings();
    }

    public static GeneralSettings getInstance() {
        if (instance == null) {
            instance = new GeneralSettings();
        }

        return instance;
    }

    public String getHostname() {
        return hostName;
    }

    public String getPassword() {
        return password;
    }

    public int getPortNumber() {
        return portNumber;
    }

    private synchronized void loadSettings() {
        try (FileInputStream fin = new FileInputStream(Constants.GLOBAL_PROP_FILE)) {
            properties.load(fin);
            hostName = properties.getProperty(PROP_HOST);
            String number = properties.getProperty(PROP_PORT);
            portNumber = Integer.parseInt(number);
            password = properties.getProperty(PROP_PASS);
        } catch (IOException e) {
            // Do nothing :)
        }
    }
}
