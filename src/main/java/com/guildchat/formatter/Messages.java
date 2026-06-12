package com.guildchat.formatter;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    
    private static final Map<String, Map<Language, String>> messages = new HashMap<>();
    
    // Message keys
    public static final String MOD_LOADED = "mod.loaded";
    public static final String BRIDGE_STATUS = "bridge.status";
    public static final String BRIDGE_STATUS_MODE_ALL = "bridge.status.mode.all";
    public static final String BRIDGE_STATUS_MODE_BRIDGE = "bridge.status.mode.bridge";
    public static final String BRIDGE_STATUS_RANDOM_ON = "bridge.status.random.on";
    public static final String BRIDGE_STATUS_RANDOM_OFF = "bridge.status.random.off";
    
    // Update messages
    public static final String UPDATE_AVAILABLE = "update.available";
    public static final String UPDATE_CHECKING = "update.checking";
    public static final String UPDATE_UP_TO_DATE = "update.up_to_date";
    public static final String UPDATE_CHECK_FAILED = "update.check_failed";
    public static final String UPDATE_DEV_VERSION = "update.dev_version";

    // Color names
    public static final String COLOR_BLACK = "color.name.black";
    public static final String COLOR_DARK_BLUE = "color.name.dark_blue";
    public static final String COLOR_DARK_GREEN = "color.name.dark_green";
    public static final String COLOR_DARK_AQUA = "color.name.dark_aqua";
    public static final String COLOR_DARK_RED = "color.name.dark_red";
    public static final String COLOR_DARK_PURPLE = "color.name.dark_purple";
    public static final String COLOR_GOLD = "color.name.gold";
    public static final String COLOR_GRAY = "color.name.gray";
    public static final String COLOR_DARK_GRAY = "color.name.dark_gray";
    public static final String COLOR_BLUE = "color.name.blue";
    public static final String COLOR_GREEN = "color.name.green";
    public static final String COLOR_AQUA = "color.name.aqua";
    public static final String COLOR_RED = "color.name.red";
    public static final String COLOR_LIGHT_PURPLE = "color.name.light_purple";
    public static final String COLOR_YELLOW = "color.name.yellow";
    public static final String COLOR_WHITE = "color.name.white";
    
    static {
        // Mod loaded
        add(MOD_LOADED, Language.ENGLISH, "GuildZip loaded!");
        add(MOD_LOADED, Language.FRENCH, "GuildZip chargé !");
        
        // Bridge commands
        add(BRIDGE_STATUS, Language.ENGLISH, "§7Bot: §e%s §7| Alias: §b%s §7| Colors: §b%s §7/ §3%s §7| Prefixes: §b%s §7/ §3%s §7| Mode: §e%s §7| Random: %s");
        add(BRIDGE_STATUS, Language.FRENCH, "§7Bot: §e%s §7| Alias: §b%s §7| Couleurs: §b%s §7/ §3%s §7| Préfixes: §b%s §7/ §3%s §7| Mode: §e%s §7| Aléatoire: %s");
        
        add(BRIDGE_STATUS_MODE_ALL, Language.ENGLISH, "all");
        add(BRIDGE_STATUS_MODE_ALL, Language.FRENCH, "tous");
        
        add(BRIDGE_STATUS_MODE_BRIDGE, Language.ENGLISH, "bridge");
        add(BRIDGE_STATUS_MODE_BRIDGE, Language.FRENCH, "bridge");

        add(BRIDGE_STATUS_RANDOM_ON, Language.ENGLISH, "§aon");
        add(BRIDGE_STATUS_RANDOM_ON, Language.FRENCH, "§aactivé");

        add(BRIDGE_STATUS_RANDOM_OFF, Language.ENGLISH, "§coff");
        add(BRIDGE_STATUS_RANDOM_OFF, Language.FRENCH, "§cdésactivé");
        
        // Color names
        add(COLOR_BLACK, Language.ENGLISH, "black");
        add(COLOR_BLACK, Language.FRENCH, "noir");
        
        add(COLOR_DARK_BLUE, Language.ENGLISH, "dark blue");
        add(COLOR_DARK_BLUE, Language.FRENCH, "bleu foncé");
        
        add(COLOR_DARK_GREEN, Language.ENGLISH, "dark green");
        add(COLOR_DARK_GREEN, Language.FRENCH, "vert foncé");
        
        add(COLOR_DARK_AQUA, Language.ENGLISH, "dark cyan");
        add(COLOR_DARK_AQUA, Language.FRENCH, "cyan foncé");
        
        add(COLOR_DARK_RED, Language.ENGLISH, "dark red");
        add(COLOR_DARK_RED, Language.FRENCH, "rouge foncé");
        
        add(COLOR_DARK_PURPLE, Language.ENGLISH, "dark purple");
        add(COLOR_DARK_PURPLE, Language.FRENCH, "violet foncé");
        
        add(COLOR_GOLD, Language.ENGLISH, "gold");
        add(COLOR_GOLD, Language.FRENCH, "or");
        
        add(COLOR_GRAY, Language.ENGLISH, "gray");
        add(COLOR_GRAY, Language.FRENCH, "gris");
        
        add(COLOR_DARK_GRAY, Language.ENGLISH, "dark gray");
        add(COLOR_DARK_GRAY, Language.FRENCH, "gris foncé");
        
        add(COLOR_BLUE, Language.ENGLISH, "blue");
        add(COLOR_BLUE, Language.FRENCH, "bleu");
        
        add(COLOR_GREEN, Language.ENGLISH, "green");
        add(COLOR_GREEN, Language.FRENCH, "vert");
        
        add(COLOR_AQUA, Language.ENGLISH, "cyan");
        add(COLOR_AQUA, Language.FRENCH, "cyan");
        
        add(COLOR_RED, Language.ENGLISH, "red");
        add(COLOR_RED, Language.FRENCH, "rouge");
        
        add(COLOR_LIGHT_PURPLE, Language.ENGLISH, "light purple");
        add(COLOR_LIGHT_PURPLE, Language.FRENCH, "rose clair");
        
        add(COLOR_YELLOW, Language.ENGLISH, "yellow");
        add(COLOR_YELLOW, Language.FRENCH, "jaune");
        
        add(COLOR_WHITE, Language.ENGLISH, "white");
        add(COLOR_WHITE, Language.FRENCH, "blanc");
        
        // Update messages
        add(UPDATE_AVAILABLE, Language.ENGLISH, "§eNew update available! §7v%s (current: v%s) §6— click to open Modrinth.");
        add(UPDATE_AVAILABLE, Language.FRENCH, "§eNouvelle mise à jour disponible ! §7v%s (actuelle: v%s) §6— clique pour ouvrir Modrinth.");

        add(UPDATE_CHECKING, Language.ENGLISH, "§eChecking for updates...");
        add(UPDATE_CHECKING, Language.FRENCH, "§eVérification des mises à jour...");
        
        add(UPDATE_UP_TO_DATE, Language.ENGLISH, "§aYou are using the latest version! §7(v%s)");
        add(UPDATE_UP_TO_DATE, Language.FRENCH, "§aVous utilisez la dernière version ! §7(v%s)");
        
        add(UPDATE_CHECK_FAILED, Language.ENGLISH, "§cFailed to check for updates. Please check your internet connection.");
        add(UPDATE_CHECK_FAILED, Language.FRENCH, "§cImpossible de vérifier les mises à jour. Vérifiez votre connexion internet.");
        
        add(UPDATE_DEV_VERSION, Language.ENGLISH, "§aYou are using a development version! §7(current: v%s, latest stable: v%s)");
        add(UPDATE_DEV_VERSION, Language.FRENCH, "§aVous utilisez une version de développement ! §7(actuelle: v%s, dernière stable: v%s)");
    }
    
    private static void add(String key, Language language, String message) {
        messages.computeIfAbsent(key, k -> new HashMap<>()).put(language, message);
    }
    
    public static String get(String key) {
        return get(key, BridgeConfig.get().getLanguage());
    }
    
    public static String get(String key, Language language) {
        Map<Language, String> langMap = messages.get(key);
        if (langMap == null) return key;
        String msg = langMap.get(language);
        return msg != null ? msg : langMap.get(Language.ENGLISH); // Fallback to English
    }
    
    public static String format(String key, Object... args) {
        return String.format(get(key), args);
    }
}
