package com.guildchat.formatter;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class UpdateNotifier {
    
    private static boolean hasChecked = false;
    private static boolean updatePendingRestart = false;
    
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Afficher le message de mise à jour au premier join du serveur
            if (!hasChecked && client.player != null) {
                hasChecked = true;

                runUpdateFlow(client, false);
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (updatePendingRestart && isHypixelServer(client)) {
                sendClientMessage(client, Messages.get(Messages.UPDATE_RESTART_ON_HYPIXEL_JOIN));
            }
        });
    }

    private static boolean isHypixelServer(MinecraftClient client) {
        if (client == null || client.getCurrentServerEntry() == null || client.getCurrentServerEntry().address == null) {
            return false;
        }
        String address = client.getCurrentServerEntry().address.toLowerCase();
        return address.contains("hypixel.net");
    }

    private static void runUpdateFlow(MinecraftClient client, boolean manual) {
        VersionManager.resetVersionCache();
        String currentVersion = VersionManager.CURRENT_VERSION != null ? VersionManager.CURRENT_VERSION : "unknown";

        VersionManager.checkVersionUpdateAsyncInternal().thenRun(() -> {
            String latestVersion = VersionManager.getLatestVersionOnline();
            if (latestVersion == null) {
                if (manual) {
                    sendClientMessage(client, Messages.get(Messages.UPDATE_CHECK_FAILED));
                }
                return;
            }

            int comparison = VersionManager.compareVersions(currentVersion, latestVersion);

            if (comparison < 0) {
                sendClientMessage(client,
                    Messages.format(Messages.UPDATE_AVAILABLE, latestVersion, currentVersion));
                sendClientMessage(client, Messages.get(Messages.UPDATE_MODRINTH));

                if (BridgeConfig.get().autoUpdaterEnabled) {
                    startAutoDownload(client, latestVersion);
                }
                return;
            }

            if (comparison > 0) {
                sendClientMessage(client,
                    Messages.format(Messages.UPDATE_DEV_VERSION, currentVersion, latestVersion));
                return;
            }

            if (manual) {
                sendClientMessage(client,
                    Messages.format(Messages.UPDATE_UP_TO_DATE, currentVersion));
            }
        });
    }

    private static void startAutoDownload(MinecraftClient client, String latestVersion) {
        VersionManager.ReleaseInfo releaseInfo = VersionManager.getLatestReleaseInfo();
        if (releaseInfo == null || releaseInfo.getJarDownloadUrl() == null || releaseInfo.getJarName() == null) {
            sendClientMessage(client, Messages.get(Messages.UPDATE_AUTO_NOT_AVAILABLE));
            return;
        }

        sendClientMessage(client, Messages.format(Messages.UPDATE_AUTO_START, latestVersion));

        UpdateDownloader.downloadLatestReleaseAsync().thenAccept(result -> {
            if (result.isSuccess()) {
                updatePendingRestart = true;
                sendClientMessage(client, Messages.format(Messages.UPDATE_AUTO_SUCCESS, result.getMessage()));
                sendClientMessage(client, Messages.get(Messages.UPDATE_RESTART_REQUIRED));
            } else {
                sendClientMessage(client, Messages.format(Messages.UPDATE_AUTO_FAILED, result.getMessage()));
            }
        });
    }

    private static void sendClientMessage(MinecraftClient client, String msg) {
        if (client == null) return;
        client.execute(() -> {
            if (client.player != null) {
                client.player.sendMessage(Text.literal(msg), false);
            }
        });
    }
    
    /**
     * Vérifie manuellement les mises à jour (pour commande)
     */
    public static void checkUpdateManually(MinecraftClient client) {
        sendClientMessage(client, Messages.get(Messages.UPDATE_CHECKING));
        runUpdateFlow(client, true);
    }
}
