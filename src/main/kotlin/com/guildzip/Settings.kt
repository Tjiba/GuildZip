package com.guildzip

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import java.awt.Color

object Settings : ConfigKt("guildzip/config") {
    override val name: TranslatableValue
        get() = Literal("GuildZip")

    // Shown on the main page when opening /gz
    var botMcName by string("") {
        name = Translated("Bot MC Name")
        description = Translated("Minecraft name of the bridge bot — only this account's messages are formatted (empty = auto-detect)")
    }
    var botAlias by string("Bridge") {
        name = Translated("Bridge Alias")
        description = Translated("Name shown instead of the bot's Minecraft name")
    }
    var guildPrefix by string("G") {
        name = Translated("Guild Prefix")
        description = Translated("Prefix for guild chat")
    }
    var officerPrefix by string("O") {
        name = Translated("Officer Prefix")
        description = Translated("Prefix for officer chat")
    }
    var formatAllGuild by boolean(true) {
        name = Translated("Format All Guild Messages")
        description = Translated("Format every guild message, not just Discord bridge relays")
    }
    var updateNotifications by boolean(true) {
        name = Translated("Update Notifications")
        description = Translated("Show a chat message on Hypixel when a new version is available")
    }

    init {
        category(Colors)
        category(VersionTags)
    }
}

object Colors : CategoryKt("Colors") {
    var guildPrefixColor by color(Color(0x55FF55).rgb) { name = Translated("Guild Prefix Color") }
    var officerPrefixColor by color(Color(0xFF55FF).rgb) { name = Translated("Officer Prefix Color") }
    var botAliasColor by color(Color(0x55FFFF).rgb) { name = Translated("Bridge Alias Color") }
    var discordNameColor by color(Color(0x00AAAA).rgb) { name = Translated("Player Color") }
    var randomMode by boolean(false) {
        name = Translated("Random Colors")
        description = Translated("Pick a random color for each message (overrides the colors above)")
    }
}

object VersionTags : CategoryKt("Version tags") {
    var versionFormattingEnabled by boolean(true) {
        name = Translated("Enable Version Tags")
        description = Translated("Show the guild version label (V1/V2/V3) instead of the bridge alias when detected")
    }
    var v1Color by color(Color(0x55FF55).rgb) { name = Translated("V1 Color") }
    var v2Color by color(Color(0xFFFF55).rgb) { name = Translated("V2 Color") }
    var v3Color by color(Color(0xFF5555).rgb) { name = Translated("V3 Color") }
}
