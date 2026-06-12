package com.guildchat.formatter;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Affiche un simple message dans le chat ~5 s après la connexion à Hypixel
 * quand une nouvelle version est disponible sur Modrinth.
 * Aucun téléchargement automatique : le joueur met à jour lui-même.
 */
public class UpdateNotifier {

    private static final long NOTIFY_DELAY_SECONDS = 5;
    private static final String MODRINTH_PAGE = "https://modrinth.com/mod/guildzip";

    private static boolean notifiedThisSession = false;

    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (notifiedThisSession || BridgeConfig.get().hideUpdateNotification) return;
            if (!isHypixelServer(client)) return;

            CompletableFuture.delayedExecutor(NOTIFY_DELAY_SECONDS, TimeUnit.SECONDS).execute(() ->
                VersionManager.checkVersionUpdateAsyncInternal().thenRun(() -> {
                    String latestVersion = VersionManager.getLatestVersionOnline();
                    if (latestVersion == null || notifiedThisSession) return;
                    if (VersionManager.compareVersions(VersionManager.CURRENT_VERSION, latestVersion) >= 0) return;
                    notifiedThisSession = true;
                    sendClientMessage(client, buildUpdateMessage(latestVersion));
                }));
        });
    }

    private static boolean isHypixelServer(Minecraft client) {
        if (client == null || client.getCurrentServer() == null || client.getCurrentServer().ip == null) {
            return false;
        }
        String address = client.getCurrentServer().ip.toLowerCase();
        return address.contains("hypixel.net");
    }

    private static Component buildUpdateMessage(String latestVersion) {
        MutableComponent msg = Component.literal("")
            .append(buildGuildZipPrefix())
            .append(Component.literal(" " + Messages.format(Messages.UPDATE_AVAILABLE,
                latestVersion, VersionManager.CURRENT_VERSION)));

        return msg.withStyle(s -> s
            .withClickEvent(new ClickEvent.OpenUrl(URI.create(MODRINTH_PAGE)))
            .withHoverEvent(new HoverEvent.ShowText(Component.literal(MODRINTH_PAGE))));
    }

    private static MutableComponent buildGuildZipPrefix() {
        return Component.literal("[").withStyle(s -> s.withColor(0x8F96BE).withBold(true))
            .append(Component.literal("G").withStyle(s -> s.withColor(0x8886B4).withBold(true)))
            .append(Component.literal("u").withStyle(s -> s.withColor(0x8175AA).withBold(true)))
            .append(Component.literal("i").withStyle(s -> s.withColor(0x7A65A1).withBold(true)))
            .append(Component.literal("l").withStyle(s -> s.withColor(0x735597).withBold(true)))
            .append(Component.literal("d").withStyle(s -> s.withColor(0x6C448D).withBold(true)))
            .append(Component.literal("Z").withStyle(s -> s.withColor(0x653483).withBold(true)))
            .append(Component.literal("i").withStyle(s -> s.withColor(0x5E247A).withBold(true)))
            .append(Component.literal("p").withStyle(s -> s.withColor(0x571370).withBold(true)))
            .append(Component.literal("]").withStyle(s -> s.withColor(0x500366).withBold(true)));
    }

    public static void checkUpdateManually(Minecraft client) {
        sendClientMessage(client, Component.literal(Messages.get(Messages.UPDATE_CHECKING)));
        VersionManager.resetVersionCache();
        VersionManager.checkVersionUpdateAsyncInternal().thenRun(() -> {
            String latestVersion = VersionManager.getLatestVersionOnline();
            if (latestVersion == null) {
                sendClientMessage(client, Component.literal(Messages.get(Messages.UPDATE_CHECK_FAILED)));
                return;
            }

            int comparison = VersionManager.compareVersions(VersionManager.CURRENT_VERSION, latestVersion);
            if (comparison < 0) {
                sendClientMessage(client, buildUpdateMessage(latestVersion));
            } else if (comparison > 0) {
                sendClientMessage(client, Component.literal(
                    Messages.format(Messages.UPDATE_DEV_VERSION, VersionManager.CURRENT_VERSION, latestVersion)));
            } else {
                sendClientMessage(client, Component.literal(
                    Messages.format(Messages.UPDATE_UP_TO_DATE, VersionManager.CURRENT_VERSION)));
            }
        });
    }

    private static void sendClientMessage(Minecraft client, Component msg) {
        if (client == null) return;
        client.execute(() -> {
            if (client.player != null) {
                client.player.sendSystemMessage(msg);
            }
        });
    }
}
