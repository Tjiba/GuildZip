package com.guildzip

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory
import java.nio.file.Files
import kotlin.io.path.listDirectoryEntries

object GuildZipMod : ClientModInitializer {
    @JvmField val LOGGER = LoggerFactory.getLogger("guildzip")
    private var pendingConfig = false

    override fun onInitializeClient() {
        LOGGER.info(Msg.MOD_LOADED.get())
        Config.get()
        cleanOldJars()
        Updater.init()

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommands.literal("gz")
                    .then(ClientCommands.literal("update").executes {
                        Updater.checkManually(it.source.client); 1
                    })
                    .executes {
                        pendingConfig = true; 1
                    }
            )
        }

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (!pendingConfig) return@register
            pendingConfig = false
            val screen = openConfig(null)
            if (screen != null) showScreen(client, screen)
            else feedback(client, "Install Cloth Config to open the settings screen.")
        }
    }

    private fun cleanOldJars() {
        val cur = Updater.CURRENT_VERSION.lowercase()
        runCatching {
            FabricLoader.getInstance().gameDir.resolve("mods").listDirectoryEntries().forEach { p ->
                val name = p.fileName.toString().lowercase()
                if (!name.startsWith("guildzip")) return@forEach
                val isOld = name.endsWith(".jar.old") ||
                    (name.endsWith(".jar") && !name.contains(cur))
                if (isOld) runCatching {
                    Files.delete(p)
                    LOGGER.info("Deleted old GuildZip JAR: ${p.fileName}")
                }.onFailure { LOGGER.warn("Could not delete old GuildZip JAR: ${p.fileName}") }
            }
        }.onFailure { LOGGER.warn("Error during old JAR cleanup: ${it.message}") }
    }

    private fun feedback(mc: Minecraft?, msg: String) {
        mc?.player?.sendSystemMessage(Component.literal(msg))
    }

    private fun openConfig(parent: Screen?): Screen? {
        if (!FabricLoader.getInstance().isModLoaded("cloth-config")) return null
        return runCatching { ConfigScreen.create(parent) }.getOrNull()
    }

    // 26.2 renamed Minecraft#setScreen to setScreenAndShow — call whichever exists.
    private fun showScreen(client: Minecraft, screen: Screen) {
        val cls = Minecraft::class.java
        val method = runCatching { cls.getMethod("setScreenAndShow", Screen::class.java) }.getOrNull()
            ?: cls.getMethod("setScreen", Screen::class.java)
        method.invoke(client, screen)
    }
}
