package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            System.out.println("✅ Configurazione caricata correttamente");
        } catch (IOException e) {
            System.err.println("❌ ERRORE: File config.properties non trovato!");
            System.err.println("💡 Rinomina config.properties in config.properties");
            System.exit(-1);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("❌ Chiave '" + key + "' non trovata in config.properties");
        }
        return value;
    }
}