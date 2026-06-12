package com.guildchat.formatter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class GuildChatMod implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("guildzip");

    private static String pendingConfigModId = null;

    @Override
    public void onInitializeClient() {
        LOGGER.info(Messages.get(Messages.MOD_LOADED));
        BridgeConfig.get(); // initialise la config
        cleanOldJars();

        // Initialiser le notificateur de mise à jour (vérifie en ligne sur Modrinth)
        UpdateNotifier.init();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(
                ClientCommands.literal("gz")
                    .then(ClientCommands.literal("update")
                        .executes(ctx -> {
                            UpdateNotifier.checkUpdateManually(ctx.getSource().getClient());
                            return 1;
                        })
                    )
                    .executes(ctx -> {
                        Minecraft client = ctx.getSource().getClient();
                        if (isModMenuLoaded()) {
                            pendingConfigModId = "guildzip";
                            return 1;
                        }
                        feedback(client, "Mod Menu is not installed.");
                        return 0;
                    })
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (pendingConfigModId == null) return;
            Screen configScreen = getConfigScreen(pendingConfigModId, client.screen);
            pendingConfigModId = null;
            if (configScreen != null) {
                client.setScreen(configScreen);
            } else {
                feedback(client, "Failed to open config screen.");
            }
        });
    }

    private static void cleanOldJars() {
        String currentVersion = VersionManager.CURRENT_VERSION;
        try {
            Path modsDir = FabricLoader.getInstance().getGameDir().resolve("mods");
            try (Stream<Path> entries = Files.list(modsDir)) {
                entries.filter(p -> {
                    String name = p.getFileName().toString().toLowerCase();
                    if (!name.startsWith("guildzip")) return false;
                    if (name.endsWith(".jar.old")) return true;
                    // Supprime tout JAR GuildZip qui n'est pas la version courante
                    if (name.endsWith(".jar") && currentVersion != null) {
                        return !name.contains(currentVersion.toLowerCase());
                    }
                    return false;
                }).forEach(old -> {
                    try {
                        Files.delete(old);
                        LOGGER.info("Deleted old GuildZip JAR: " + old.getFileName());
                    } catch (Exception e) {
                        LOGGER.warn("Could not delete old GuildZip JAR: " + old.getFileName());
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.warn("Error during old JAR cleanup: " + e.getMessage());
        }
    }

    private static void feedback(Minecraft mc, String msg) {
        if (mc != null && mc.player != null)
            mc.player.sendSystemMessage(Component.literal(msg));
    }

    private static boolean isModMenuLoaded() {
        return FabricLoader.getInstance().isModLoaded("modmenu");
    }

    private static Screen getConfigScreen(String modId, Screen parent) {
        try {
            Class<?> modMenuClass = Class.forName("com.terraformersmc.modmenu.ModMenu");
            Method getConfigScreen = modMenuClass.getMethod("getConfigScreen", String.class, Screen.class);
            Object screen = getConfigScreen.invoke(null, modId, parent);
            return (Screen) screen;
        } catch (ReflectiveOperationException | RuntimeException e) {
            return null;
        }
    }
}