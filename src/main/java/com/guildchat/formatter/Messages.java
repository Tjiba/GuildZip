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
    public static final String UPDATE_MODRINTH = "update.modrinth";
    public static final String UPDATE_CHECKING = "update.checking";
    public static final String UPDATE_UP_TO_DATE = "update.up_to_date";
    public static final String UPDATE_CHECK_FAILED = "update.check_failed";
    public static final String UPDATE_DEV_VERSION = "update.dev_version";
    public static final String UPDATE_AUTO_START = "update.auto.start";
    public static final String UPDATE_AUTO_SUCCESS = "update.auto.success";
    public static final String UPDATE_AUTO_FAILED = "update.auto.failed";
    public static final String UPDATE_RESTART_REQUIRED = "update.restart.required";
    public static final String UPDATE_RESTART_ON_HYPIXEL_JOIN = "update.restart.on_hypixel_join";
    public static final String UPDATE_AUTO_NOT_AVAILABLE = "update.auto.not_available";

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
        add(UPDATE_AVAILABLE, Language.ENGLISH, "§c§lGuildZip update available! §7v%s is now available (current: v%s). §6Download it on §lModrinth§6.");
        add(UPDATE_AVAILABLE, Language.FRENCH, "§c§lMise à jour disponible pour GuildZip ! §7v%s est maintenant disponible (actuelle: v%s). §6Télécharger sur §lModrinth§6.");
        
        add(UPDATE_MODRINTH, Language.ENGLISH, "§6Link: §bhttps://modrinth.com/mod/guildzip");
        add(UPDATE_MODRINTH, Language.FRENCH, "§6Lien: §bhttps://modrinth.com/mod/guildzip");
        
        add(UPDATE_CHECKING, Language.ENGLISH, "§eChecking for updates...");
        add(UPDATE_CHECKING, Language.FRENCH, "§eVérification des mises à jour...");
        
        add(UPDATE_UP_TO_DATE, Language.ENGLISH, "§aYou are using the latest version! §7(v%s)");
        add(UPDATE_UP_TO_DATE, Language.FRENCH, "§aVous utilisez la dernière version ! §7(v%s)");
        
        add(UPDATE_CHECK_FAILED, Language.ENGLISH, "§cFailed to check for updates. Please check your internet connection.");
        add(UPDATE_CHECK_FAILED, Language.FRENCH, "§cImpossible de vérifier les mises à jour. Vérifiez votre connexion internet.");
        
        add(UPDATE_DEV_VERSION, Language.ENGLISH, "§aYou are using a development version! §7(current: v%s, latest stable: v%s)");
        add(UPDATE_DEV_VERSION, Language.FRENCH, "§aVous utilisez une version de développement ! §7(actuelle: v%s, dernière stable: v%s)");

        add(UPDATE_AUTO_START, Language.ENGLISH, "§eAuto-updater: downloading v%s...");
        add(UPDATE_AUTO_START, Language.FRENCH, "§eAuto-updater : téléchargement de la v%s...");

        add(UPDATE_AUTO_SUCCESS, Language.ENGLISH, "§aAuto-updater: %s");
        add(UPDATE_AUTO_SUCCESS, Language.FRENCH, "§aAuto-updater : %s");

        add(UPDATE_AUTO_FAILED, Language.ENGLISH, "§cAuto-updater failed: %s");
        add(UPDATE_AUTO_FAILED, Language.FRENCH, "§cAuto-updater échoué : %s");

        add(UPDATE_RESTART_REQUIRED, Language.ENGLISH, "§6Restart Minecraft to load the new version.");
        add(UPDATE_RESTART_REQUIRED, Language.FRENCH, "§6Redémarrez Minecraft pour charger la nouvelle version.");

        add(UPDATE_RESTART_ON_HYPIXEL_JOIN, Language.ENGLISH, "§6You joined Hypixel. The downloaded update will be active after restarting Minecraft.");
        add(UPDATE_RESTART_ON_HYPIXEL_JOIN, Language.FRENCH, "§6Tu as rejoint Hypixel. La mise à jour téléchargée sera active après avoir redémarré Minecraft.");

        add(UPDATE_AUTO_NOT_AVAILABLE, Language.ENGLISH, "§cAuto-updater unavailable: no downloadable JAR found on latest release.");
        add(UPDATE_AUTO_NOT_AVAILABLE, Language.FRENCH, "§cAuto-updater indisponible : aucun JAR téléchargeable trouvé sur la dernière release.");
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
