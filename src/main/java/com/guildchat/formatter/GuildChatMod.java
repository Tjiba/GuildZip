package com.guildchat.formatter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
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
                ClientCommandManager.literal("gz")
                    .then(ClientCommandManager.literal("update")
                        .executes(ctx -> {
                            UpdateNotifier.checkUpdateManually(ctx.getSource().getClient());
                            return 1;
                        })
                    )
                    .executes(ctx -> {
                        MinecraftClient client = ctx.getSource().getClient();
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
            Screen configScreen = getConfigScreen(pendingConfigModId, client.currentScreen);
            pendingConfigModId = null;
            if (configScreen != null) {
                client.setScreen(configScreen);
            } else {
                feedback(client, "Failed to open config screen.");
            }
        });
    }

    private static void cleanOldJars() {
        try {
            Path modsDir = FabricLoader.getInstance().getGameDir().resolve("mods");
            try (Stream<Path> entries = Files.list(modsDir)) {
                entries.filter(p -> {
                    String name = p.getFileName().toString().toLowerCase();
                    return name.startsWith("guildzip") && name.endsWith(".jar.old");
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

    private static void feedback(MinecraftClient mc, String msg) {
        if (mc != null && mc.player != null)
            mc.player.sendMessage(Text.literal(msg), false);
    }

    private static String safeColorCode(String code) {
        if (code == null || code.isEmpty()) return "b";
        return code.substring(0, 1).toLowerCase();
    }


    private static String colorNameFromCode(String code) {
        String safe = safeColorCode(code);
        return switch (safe) {
            case "0" -> Messages.get(Messages.COLOR_BLACK);
            case "1" -> Messages.get(Messages.COLOR_DARK_BLUE);
            case "2" -> Messages.get(Messages.COLOR_DARK_GREEN);
            case "3" -> Messages.get(Messages.COLOR_DARK_AQUA);
            case "4" -> Messages.get(Messages.COLOR_DARK_RED);
            case "5" -> Messages.get(Messages.COLOR_DARK_PURPLE);
            case "6" -> Messages.get(Messages.COLOR_GOLD);
            case "7" -> Messages.get(Messages.COLOR_GRAY);
            case "8" -> Messages.get(Messages.COLOR_DARK_GRAY);
            case "9" -> Messages.get(Messages.COLOR_BLUE);
            case "a" -> Messages.get(Messages.COLOR_GREEN);
            case "b" -> Messages.get(Messages.COLOR_AQUA);
            case "c" -> Messages.get(Messages.COLOR_RED);
            case "d" -> Messages.get(Messages.COLOR_LIGHT_PURPLE);
            case "e" -> Messages.get(Messages.COLOR_YELLOW);
            case "f" -> Messages.get(Messages.COLOR_WHITE);
            default -> Messages.get(Messages.COLOR_AQUA);
        };
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