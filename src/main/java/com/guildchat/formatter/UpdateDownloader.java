package com.guildchat.formatter;

import net.fabricmc.loader.api.FabricLoader;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

public class UpdateDownloader {

    public static class DownloadResult {
        private final boolean success;
        private final String message;
        private final Path targetPath;

        public DownloadResult(boolean success, String message, Path targetPath) {
            this.success = success;
            this.message = message;
            this.targetPath = targetPath;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Path getTargetPath() {
            return targetPath;
        }
    }

    public static CompletableFuture<DownloadResult> downloadLatestReleaseAsync() {
        return CompletableFuture.supplyAsync(UpdateDownloader::downloadLatestRelease);
    }

    public static DownloadResult downloadLatestRelease() {
        try {
            VersionManager.ReleaseInfo releaseInfo = VersionManager.getLatestReleaseInfo();
            if (releaseInfo == null || releaseInfo.getJarDownloadUrl() == null || releaseInfo.getJarName() == null) {
                return new DownloadResult(false, "No downloadable release asset found.", null);
            }

            URI downloadUri = URI.create(releaseInfo.getJarDownloadUrl());
            String host = downloadUri.getHost();
            if (host == null || (!host.equals("github.com") && !host.equals("objects.githubusercontent.com"))) {
                return new DownloadResult(false, "Refused update: untrusted download host.", null);
            }

            Path modsDir = FabricLoader.getInstance().getGameDir().resolve("mods");
            if (!Files.exists(modsDir)) {
                Files.createDirectories(modsDir);
            }

            String cleanName = releaseInfo.getJarName().replace(" ", "-");
            Path finalPath = modsDir.resolve(cleanName);
            Path tmpPath = modsDir.resolve(cleanName + ".download");

            HttpURLConnection connection = (HttpURLConnection) downloadUri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("User-Agent", "GuildChatShortener-Mod-Updater");

            int responseCode = connection.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                return new DownloadResult(false, "Download failed: HTTP " + responseCode, null);
            }

            try (InputStream in = connection.getInputStream();
                 OutputStream out = Files.newOutputStream(tmpPath)) {
                in.transferTo(out);
            }

            Files.move(tmpPath, finalPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            return new DownloadResult(true, "Update downloaded to: " + finalPath.getFileName(), finalPath);
        } catch (Exception e) {
            GuildChatMod.LOGGER.error("Auto-update download failed", e);
            return new DownloadResult(false, "Download failed: " + e.getMessage(), null);
        }
    }
}

