package com.guildzip

import java.util.concurrent.ThreadLocalRandom
import java.util.regex.Pattern

/**
 * Parse un message brut de guilde Hypixel et produit les segments colorés (RGB) à afficher,
 * ou null pour laisser le message inchangé. Aucun type Minecraft ici → testable hors jeu.
 */
object ChatFormatter {

    data class Seg(@JvmField val text: String, @JvmField val color: Int?)

    private val COLOR_CODE = Pattern.compile("[§&][0-9a-fk-orA-FK-OR]")
    private val BRIDGE_HEADER = Pattern.compile(
        "^(Guild|Officer|G|O) > (?:\\[([A-Z+]+)] )?([\\w]+)(?:\\s+\\[([A-Za-z0-9+_]+)])?:\\s*(.+)$")
    private val CHANNEL_MARKER = Pattern.compile("^([A-Z0-9]{1,2}) > .+")
    private val DISCORD_WARNING_SUFFIX = Pattern.compile("\\s*Please be mindful of Discord links in chat as they may pose a security risk\\s*$")
    private val GUILD_VERSION_TAG = Pattern.compile("^\\[(V[123])]\\s+", Pattern.CASE_INSENSITIVE)
    private val CHAT_TIMESTAMP_PREFIX = Pattern.compile("^\\[[0-2]\\d:[0-5]\\d:[0-5]\\d]\\s+")

    private val RANDOM_COLORS = intArrayOf(
        0x0000AA, 0x00AA00, 0x00AAAA, 0xAA0000, 0xAA00AA, 0xFFAA00,
        0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF)

    private const val SEP = 0x555555   // séparateurs (gris foncé)
    private const val WHITE = 0xFFFFFF

    fun format(input: String): List<Seg>? {
        val hadWarning = input.contains("Please be mindful of Discord links")
        val base = if (hadWarning) DISCORD_WARNING_SUFFIX.matcher(input).replaceAll("") else input
        val unchanged = if (hadWarning) listOf(Seg(base, null)) else null

        var raw = COLOR_CODE.matcher(base).replaceAll("")
        raw = raw.replace(Regex("\\s+"), " ").trim()
        raw = CHAT_TIMESTAMP_PREFIX.matcher(raw).replaceFirst("")
        raw = DISCORD_WARNING_SUFFIX.matcher(raw).replaceAll("").trim()

        if (!raw.startsWith("Guild > ") && !raw.startsWith("Officer > ")
            && !raw.startsWith("G > ") && !raw.startsWith("O > ")) return unchanged

        val m = BRIDGE_HEADER.matcher(raw)
        if (!m.matches()) return unchanged

        val headerChannel = m.group(1)
        val botMC = m.group(3)
        val payload = m.group(5)

        val botFilter = Settings.botMcName.ifEmpty { null }
        if (botFilter != null && !botFilter.equals(botMC, ignoreCase = true)) return unchanged

        val channelMarker = extractChannelMarker(payload)
        val isBridgePayload = hasChannelMarker(payload) || hasGuildVersionTag(payload)
        val isOfficer = headerChannel.equals("Officer", true) || "O".equals(channelMarker, true)
        if (!isBridgePayload && !Settings.formatAllGuild) return unchanged

        val prefix = resolvePrefix(isOfficer)
        val prefixColor = colorOf(if (isOfficer) Colors.officerPrefixColor else Colors.guildPrefixColor)

        if (!isBridgePayload) {
            val message = payload.trim()
            if (message.isEmpty()) return unchanged
            return listOf(
                Seg(prefix, prefixColor), Seg(" > ", SEP),
                Seg(botMC, colorOf(Colors.discordNameColor)), Seg(": ", SEP),
                Seg(message, WHITE))
        }

        var cleaned = stripLeadingNonVersionTag(payload)
        var guildVersion: String? = null
        val vm = GUILD_VERSION_TAG.matcher(cleaned)
        if (vm.find()) {
            guildVersion = vm.group(1).uppercase()
            cleaned = cleaned.substring(vm.end()).trim()
        }
        cleaned = stripLeadingNonVersionTag(cleaned)

        val marker = extractChannelMarker(cleaned)
        if (marker != null && cleaned.startsWith("$marker > ")) {
            cleaned = cleaned.substring(marker.length + 3)
        }

        val discord: String
        val message: String
        var sep = cleaned.indexOf(": ")
        if (sep >= 0) {
            discord = cleaned.substring(0, sep).trim()
            message = cleaned.substring(sep + 2).trim()
        } else {
            sep = cleaned.indexOf(" > ")
            if (sep < 0) return unchanged
            discord = cleaned.substring(0, sep).trim()
            message = cleaned.substring(sep + 3).trim()
        }
        if (discord.isEmpty() || message.isEmpty()) return unchanged

        val useVersion = VersionTags.versionFormattingEnabled && !guildVersion.isNullOrBlank()
        val versionOrAlias = if (useVersion) guildVersion!! else Settings.botAlias
        val voaColor = if (Colors.randomMode) randomColor() else versionOrAliasColor(versionOrAlias, useVersion)

        return listOf(
            Seg(prefix, prefixColor), Seg(" > ", SEP),
            Seg(versionOrAlias, voaColor), Seg(" > ", SEP),
            Seg(discord, colorOf(Colors.discordNameColor)), Seg(" : ", SEP),
            Seg(message, WHITE))
    }

    private fun colorOf(color: Int) = if (Colors.randomMode) randomColor() else (color and 0xFFFFFF)

    private fun randomColor() = RANDOM_COLORS[ThreadLocalRandom.current().nextInt(RANDOM_COLORS.size)]

    private fun versionOrAliasColor(versionOrAlias: String, useVersion: Boolean): Int {
        if (!useVersion) return Colors.botAliasColor and 0xFFFFFF
        return when (versionOrAlias.uppercase()) {
            "V1" -> VersionTags.v1Color and 0xFFFFFF
            "V2" -> VersionTags.v2Color and 0xFFFFFF
            "V3" -> VersionTags.v3Color and 0xFFFFFF
            else -> Colors.botAliasColor and 0xFFFFFF
        }
    }

    private fun extractChannelMarker(payload: String?): String? {
        if (payload.isNullOrEmpty()) return null
        val m = CHANNEL_MARKER.matcher(stripLeadingNonVersionTag(payload))
        return if (m.matches()) m.group(1) else null
    }

    private fun resolvePrefix(isOfficer: Boolean): String {
        val prefix = if (isOfficer) Settings.officerPrefix else Settings.guildPrefix
        return if (prefix.isBlank()) (if (isOfficer) "O" else "G") else prefix.trim()
    }

    private fun hasChannelMarker(payload: String?): Boolean {
        if (payload.isNullOrEmpty()) return false
        var cleaned = stripLeadingNonVersionTag(payload)
        if (cleaned.startsWith("[")) {
            val end = cleaned.indexOf("] ")
            if (end > 0 && end + 2 <= cleaned.length) {
                val after = cleaned.substring(end + 2)
                if (after.contains(": ")) return true
                cleaned = after
            }
        }
        return CHANNEL_MARKER.matcher(cleaned).matches()
    }

    private fun hasGuildVersionTag(payload: String?): Boolean {
        if (payload.isNullOrEmpty()) return false
        return GUILD_VERSION_TAG.matcher(stripLeadingNonVersionTag(payload)).find()
    }

    private fun stripLeadingNonVersionTag(payload: String): String {
        if (payload.isEmpty() || !payload.startsWith("[")) return payload
        val end = payload.indexOf("] ")
        if (end <= 0 || end + 2 > payload.length) return payload
        val tag = payload.substring(1, end)
        if (tag.equals("V1", true) || tag.equals("V2", true) || tag.equals("V3", true)) return payload
        return payload.substring(end + 2)
    }
}
