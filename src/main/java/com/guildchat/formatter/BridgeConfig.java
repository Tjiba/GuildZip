package com.guildchat.formatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class BridgeConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("guildchat-formatter.json");

    private static BridgeConfig instance;

    // Nom du compte Minecraft du bot bridge   (ex: "KetroX")
    // null = détection automatique de n'importe quel bot
    public String botMCName = null;

    // Nom affiché à la place du nom MC du bot (ex: "Bridge")
    public String botAlias = "Bridge";

    // Couleur du nom du bridge (code Minecraft sans §, ex: "b" pour aqua)
    public String botAliasColor = "b";

    // Couleur du pseudo Discord (code Minecraft sans §, ex: "3" pour dark_aqua)
    public String discordNameColor = "3";

    // Préfixe affiché pour le chat de guilde
    public String guildPrefix = "G";

    // Préfixe affiché pour le chat officier
    public String officerPrefix = "O";

    // Couleur du préfixe guilde (code Minecraft sans §, ex: "a" pour green)
    public String guildPrefixColor = "a";

    // Couleur du préfixe officier (code Minecraft sans §, ex: "d" pour magenta)
    public String officerPrefixColor = "d";

    // Active le formatage pour tous les messages de guilde (pas seulement le bridge)
    public boolean formatAllGuild = true;

    // Active le mode couleurs aléatoires pour chaque message
    public boolean randomMode = false;

    // Langue de l'interface (english ou french)
    public String language = "english";

    // Active le téléchargement automatique de la dernière version stable lorsqu'elle est disponible
    public boolean autoUpdaterEnabled = true;

    // Active l'affichage de la version de guilde (v1/v2/v3) a la place de l'alias bridge
    public boolean versionFormattingEnabled = true;

    // Couleurs dediees aux labels de version de guilde
    public String guildVersionV1Color = "a";
    public String guildVersionV2Color = "e";
    public String guildVersionV3Color = "c";

    // ── Getters ───────────────────────────────────────────────────────────────
    public Language getLanguage() {
        Language lang = Language.fromString(language);
        return lang != null ? lang : Language.ENGLISH;
    }

    // ── Singleton ─────────────────────────────────────────────────────────────
    public static BridgeConfig get() {
        if (instance == null) instance = load();
        return instance;
    }

    public static void reload() { instance = load(); }

    private static BridgeConfig load() {
        File f = FILE.toFile();
        if (f.exists()) {
            try (FileReader r = new FileReader(f)) {
                BridgeConfig cfg = GSON.fromJson(r, BridgeConfig.class);
                if (cfg != null) return cfg;
            } catch (IOException e) {
                GuildChatMod.LOGGER.error("Erreur lecture config: " + e.getMessage());
            }
        }
        BridgeConfig cfg = new BridgeConfig();
        cfg.save();
        return cfg;
    }

    public void save() {
        try {
            File dir = FILE.getParent().toFile();
            if (!dir.exists() && !dir.mkdirs()) {
                GuildChatMod.LOGGER.warn("Impossible de créer le dossier config: " + dir.getAbsolutePath());
            }
            try (FileWriter w = new FileWriter(FILE.toFile())) {
                GSON.toJson(this, w);
            }
        } catch (IOException e) {
            GuildChatMod.LOGGER.error("Erreur sauvegarde config: " + e.getMessage());
        }
    }
}