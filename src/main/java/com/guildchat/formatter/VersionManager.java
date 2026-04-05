package com.guildchat.formatter;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.HttpURLConnection;
import java.util.concurrent.CompletableFuture;

public class VersionManager {

    public static class ReleaseInfo {
        private final String version;
        private final String jarName;
        private final String jarDownloadUrl;

        public ReleaseInfo(String version, String jarName, String jarDownloadUrl) {
            this.version = version;
            this.jarName = jarName;
            this.jarDownloadUrl = jarDownloadUrl;
        }

        public String getVersion() {
            return version;
        }

        public String getJarName() {
            return jarName;
        }

        public String getJarDownloadUrl() {
            return jarDownloadUrl;
        }
    }
    
    // Version dynamique lue depuis fabric.mod.json au lieu d'être codée en dur
    public static final String CURRENT_VERSION = FabricLoader.getInstance()
            .getModContainer("guildzip")
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("unknown");
    
    private static final String MODRINTH_API_URL = "https://api.modrinth.com/v2/project/guildzip/version";
    
    private static String latestVersionOnline = null;
    private static ReleaseInfo latestReleaseInfo = null;
    
    /**
     * Vérifie la version en ligne et retourne un CompletableFuture
     * (utilisé pour la vérification asynchrone)
     */
    static CompletableFuture<Void> checkVersionUpdateAsyncInternal() {
        return CompletableFuture.runAsync(() -> {
            try {
                GuildChatMod.LOGGER.info("Checking for updates from Modrinth...");
                GuildChatMod.LOGGER.info("Current version: " + CURRENT_VERSION);
                
                ReleaseInfo releaseInfo = fetchLatestReleaseFromModrinth();
                if (releaseInfo != null && releaseInfo.getVersion() != null) {
                    latestReleaseInfo = releaseInfo;
                    latestVersionOnline = releaseInfo.getVersion();
                    GuildChatMod.LOGGER.info("Latest version on Modrinth: " + latestVersionOnline);

                    if (CURRENT_VERSION != null) {
                        int comparison = compareVersions(CURRENT_VERSION, latestVersionOnline);
                        if (comparison < 0) {
                            // Current version is older, update available
                            showUpdateMessage(latestVersionOnline);
                        } else if (comparison > 0) {
                            // Current version is newer (dev version)
                            showDevVersionMessage(latestVersionOnline);
                        } else {
                            GuildChatMod.LOGGER.info("Version is up to date!");
                        }
                    }
                } else {
                    GuildChatMod.LOGGER.warn("Failed to fetch latest version from Modrinth (returned null)");
                }
            } catch (Exception e) {
                GuildChatMod.LOGGER.error("Error checking version", e);
            }
        });
    }
    
    /**
     * Récupère la dernière version depuis l'API Modrinth
     */
    private static ReleaseInfo fetchLatestReleaseFromModrinth() throws Exception {
        GuildChatMod.LOGGER.info("Fetching from Modrinth: " + MODRINTH_API_URL);

        var url = new URI(MODRINTH_API_URL).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestProperty("User-Agent", "GuildZip-Mod");

        int responseCode = connection.getResponseCode();
        GuildChatMod.LOGGER.info("Modrinth API response code: " + responseCode);

        if (responseCode == 200) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                String jsonResponse = response.toString();
                JsonArray versions = JsonParser.parseString(jsonResponse).getAsJsonArray();

                // Prendre la première version (la plus récente)
                if (versions.size() > 0) {
                    JsonObject latestVersion = versions.get(0).getAsJsonObject();

                    if (!latestVersion.has("version_number")) {
                        GuildChatMod.LOGGER.error("No 'version_number' field in Modrinth response");
                        return null;
                    }

                    String version = latestVersion.get("version_number").getAsString();
                    GuildChatMod.LOGGER.info("Found Modrinth version: " + version);

                    // Récupérer le JAR depuis les fichiers
                    if (latestVersion.has("files") && latestVersion.get("files").isJsonArray()) {
                        JsonArray files = latestVersion.getAsJsonArray("files");
                        for (JsonElement element : files) {
                            if (!element.isJsonObject()) continue;
                            JsonObject file = element.getAsJsonObject();
                            if (!file.has("filename") || !file.has("url")) continue;

                            String filename = file.get("filename").getAsString();
                            String downloadUrl = file.get("url").getAsString();

                            if (filename.endsWith(".jar")) {
                                GuildChatMod.LOGGER.info("Selected Modrinth asset: " + filename);
                                return new ReleaseInfo(version, filename, downloadUrl);
                            }
                        }
                    }

                    return new ReleaseInfo(version, null, null);
                }
            }
        } else {
            GuildChatMod.LOGGER.warn("Modrinth API returned non-200 code: " + responseCode);
        }

        connection.disconnect();
        return null;
    }

    /**
     * Affiche le message de mise à jour
     */
    private static void showUpdateMessage(String newVersion) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            GuildChatMod.LOGGER.info("GuildZip update available! New version: " + newVersion + " (current: " + CURRENT_VERSION + ")");
        }
    }
    
    /**
     * Affiche le message de version de développement
     */
    private static void showDevVersionMessage(String releaseVersion) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            GuildChatMod.LOGGER.info("GuildZip: Dev version " + CURRENT_VERSION + " > " + releaseVersion + " (release)");
        }
    }
    
    /**
     * Retourne la dernière version disponible en ligne
     */
    public static String getLatestVersionOnline() {
        return latestVersionOnline;
    }

    public static ReleaseInfo getLatestReleaseInfo() {
        return latestReleaseInfo;
    }
    
    /**
     * Réinitialise le cache de version (utile pour forcer une nouvelle vérification)
     */
    public static void resetVersionCache() {
        latestVersionOnline = null;
        latestReleaseInfo = null;
    }
    
    /**
     * Compare deux versions sémantiques
     * @param version1 Première version (ex: "1.2.2")
     * @param version2 Deuxième version (ex: "1.2.1")
     * @return -1 si version1 < version2, 0 si égales, +1 si version1 > version2
     */
    public static int compareVersions(String version1, String version2) {
        try {
            String[] parts1 = version1.split("\\.");
            String[] parts2 = version2.split("\\.");
            
            for (int i = 0; i < Math.max(parts1.length, parts2.length); i++) {
                int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
                int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
                
                if (num1 < num2) return -1;
                if (num1 > num2) return 1;
            }
            return 0; // Versions égales
        } catch (NumberFormatException e) {
            GuildChatMod.LOGGER.warn("Erreur lors de la comparaison des versions: " + e.getMessage());
            return 0; // En cas d'erreur, on considère les versions égales
        }
    }
}

