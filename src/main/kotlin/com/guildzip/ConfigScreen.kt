package com.guildzip

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> =
        ConfigScreenFactory { parent: Screen -> ConfigScreen.create(parent) }
}

object ConfigScreen {

    fun create(parent: Screen?): Screen {
        val cfg = Config.get()
        val builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.literal("GuildZip"))
            .setSavingRunnable { cfg.save(); Config.reload() }

        val general = builder.getOrCreateCategory(Component.literal("General"))
        val colors = builder.getOrCreateCategory(Component.literal("Colors"))
        val versions = builder.getOrCreateCategory(Component.literal("Version tags"))
        val e = ConfigEntryBuilder.create()

        fun str(label: String, value: String, def: String, tip: String, save: (String) -> Unit) =
            e.startStrField(Component.literal(label), value)
                .setDefaultValue(def).setTooltip(Component.literal(tip))
                .setSaveConsumer(save).build()

        fun bool(label: String, value: Boolean, def: Boolean, tip: String, save: (Boolean) -> Unit) =
            e.startBooleanToggle(Component.literal(label), value)
                .setDefaultValue(def).setTooltip(Component.literal(tip))
                .setSaveConsumer(save).build()

        fun color(label: String, value: Int, def: Int, tip: String, save: (Int) -> Unit) =
            e.startColorField(Component.literal(label), value)
                .setDefaultValue(def).setTooltip(Component.literal(tip))
                .setSaveConsumer(save).build()

        // ── General ──────────────────────────────────────────────────────────
        general.addEntry(str("Bot MC Name", cfg.botMCName ?: "", "",
            "Minecraft name of the bridge bot — only this account's messages are formatted (empty = auto-detect)") {
            cfg.botMCName = it.ifEmpty { null }
        })
        general.addEntry(str("Bridge Alias", cfg.botAlias, "Bridge",
            "Name shown instead of the bot's Minecraft name") {
            cfg.botAlias = it.ifEmpty { "Bridge" }
        })
        general.addEntry(str("Guild Prefix", cfg.guildPrefix, "G",
            "Prefix for guild chat") {
            cfg.guildPrefix = if (it.isBlank()) "G" else it
        })
        general.addEntry(str("Officer Prefix", cfg.officerPrefix, "O",
            "Prefix for officer chat") {
            cfg.officerPrefix = if (it.isBlank()) "O" else it
        })
        general.addEntry(bool("Format All Guild Messages", cfg.formatAllGuild, false,
            "Format every guild message, not just Discord bridge relays") {
            cfg.formatAllGuild = it
        })
        general.addEntry(bool("Update Notifications", !cfg.hideUpdateNotification, true,
            "Show a chat message on Hypixel when a new version is available") {
            cfg.hideUpdateNotification = !it
        })

        // ── Colors ───────────────────────────────────────────────────────────
        colors.addEntry(color("Guild Prefix Color", cfg.guildPrefixColor, 0x55FF55,
            "Color of the guild prefix") { cfg.guildPrefixColor = it })
        colors.addEntry(color("Officer Prefix Color", cfg.officerPrefixColor, 0xFF55FF,
            "Color of the officer prefix") { cfg.officerPrefixColor = it })
        colors.addEntry(color("Bridge Alias Color", cfg.botAliasColor, 0x55FFFF,
            "Color of the bridge alias") { cfg.botAliasColor = it })
        colors.addEntry(color("Player Color", cfg.discordNameColor, 0x00AAAA,
            "Color of player / Discord names") { cfg.discordNameColor = it })
        colors.addEntry(bool("Random Colors", cfg.randomMode, false,
            "Pick a random color for each message (overrides the colors above)") { cfg.randomMode = it })

        // ── Version tags ─────────────────────────────────────────────────────
        versions.addEntry(bool("Enable Version Tags", cfg.versionFormattingEnabled, true,
            "Show the guild version label (V1/V2/V3) instead of the bridge alias when detected") {
            cfg.versionFormattingEnabled = it
        })
        versions.addEntry(color("V1 Color", cfg.guildVersionV1Color, 0x55FF55,
            "Color of the V1 label") { cfg.guildVersionV1Color = it })
        versions.addEntry(color("V2 Color", cfg.guildVersionV2Color, 0xFFFF55,
            "Color of the V2 label") { cfg.guildVersionV2Color = it })
        versions.addEntry(color("V3 Color", cfg.guildVersionV3Color, 0xFF5555,
            "Color of the V3 label") { cfg.guildVersionV3Color = it })

        return builder.build()
    }
}
