package com.guildzip

import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import java.io.FileReader
import java.io.FileWriter

class Config {
    @JvmField var botMCName: String? = null
    @JvmField var botAlias = "Bridge"
    @JvmField var botAliasColor = 0x55FFFF
    @JvmField var discordNameColor = 0x00AAAA
    @JvmField var guildPrefix = "G"
    @JvmField var officerPrefix = "O"
    @JvmField var guildPrefixColor = 0x55FF55
    @JvmField var officerPrefixColor = 0xFF55FF
    @JvmField var formatAllGuild = true
    @JvmField var randomMode = false
    @JvmField var hideUpdateNotification = false
    @JvmField var versionFormattingEnabled = true
    @JvmField var guildVersionV1Color = 0x55FF55
    @JvmField var guildVersionV2Color = 0xFFFF55
    @JvmField var guildVersionV3Color = 0xFF5555

    fun save() {
        runCatching {
            FILE.parentFile?.mkdirs()
            FileWriter(FILE).use { GSON.toJson(this, it) }
        }.onFailure { GuildZipMod.LOGGER.error("Erreur sauvegarde config: ${it.message}") }
    }

    companion object {
        private val GSON = GsonBuilder().setPrettyPrinting().create()
        private val FILE by lazy { FabricLoader.getInstance().configDir.resolve("guildzip.json").toFile() }
        private var instance: Config? = null

        @JvmStatic fun get(): Config = instance ?: load().also { instance = it }
        @JvmStatic fun reload() { instance = load() }

        private fun load(): Config {
            if (FILE.exists()) {
                runCatching { FileReader(FILE).use { GSON.fromJson(it, Config::class.java) } }
                    .getOrNull()?.let { return it }
            }
            return Config().also { it.save() }
        }
    }
}
