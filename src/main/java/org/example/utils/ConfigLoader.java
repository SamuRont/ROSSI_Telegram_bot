package org.example.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(new FileInputStream("config.properties"));
        } catch (Exception e) {
            System.err.println("Errore caricamento config");
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
