package com.guildchat.formatter.mixin;

import com.guildchat.formatter.BridgeConfig;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formate les messages bridge Discord de la guild Hypixel.
 *
 * Format Hypixel brut (après strip des §codes) :
 *   "Guild > [RANG] NomBot [ROLE]: X > PseudoDiscord: message"
 *    ─ RANG  = rang Hypixel du bot      (MVP, VIP+, etc.)  — optionnel
 *    ─ NomBot = compte Minecraft du bot  (ex: KetroX)
 *    ─ ROLE  = rôle dans la guild       (GM, OFFICER)       — optionnel
 *    ─ X     = lettre du canal Discord  (D, O, G, etc.)
 *    ─ PseudoDiscord = pseudo de l'utilisateur Discord
 *    ─ message = contenu
 *
 * Résultat affiché :
 *   "G > Bridge [GM] > PseudoDiscord: message"
 *
 * Les messages guild normaux (sans bridge) ne sont PAS modifiés.
 */
@Mixin(ChatHud.class)
public class ChatHudMixin {

    // Supprime tous les codes couleur Minecraft (§0-§9, §a-§f, §k-§r)
    @Unique
    private static final Pattern COLOR_CODE = Pattern.compile("[§&][0-9a-fk-orA-FK-OR]");

    // Pattern bridge (en-tete) :
    //   Groupe 1 = canal (Guild, Officer, G ou O)
    //   Groupe 2 = rang Hypixel du bot (optionnel)
    //   Groupe 3 = nom MC du bot
    //   Groupe 4 = rôle guild : GM ou OFFICER (optionnel)
    //   Groupe 5 = reste du message (payload)
    // Supporte les formats: "Guild > [RANG] NomBot: payload" ou "Guild > [RANG] NomBot [ROLE]: payload"
    @Unique
    private static final Pattern BRIDGE_HEADER = Pattern.compile(
        "^(Guild|Officer|G|O) > (?:\\[([A-Z+]+)] )?([\\w]+)(?:\\s+\\[([A-Za-z0-9+_]+)])?:\\s*(.+)$"
    );

    @Unique
    private static final Pattern CHANNEL_MARKER = Pattern.compile("^([A-Z0-9]{1,2}) > .+");

    @Unique
    private static final String[] RANDOM_COLORS = {
        "1", "2", "3", "4", "5", "6", "9", "a", "b", "c", "d", "e", "f"
    };
    
    // Pattern to remove the Discord security warning message that gets appended by Hypixel
    @Unique
    private static final Pattern DISCORD_WARNING_SUFFIX = Pattern.compile("\\s*Please be mindful of Discord links in chat as they may pose a security risk\\s*$");

    @Unique
    private static final Pattern GUILD_VERSION_TAG = Pattern.compile("^\\[(V[123])]\\s+", Pattern.CASE_INSENSITIVE);

    @Unique
    private static final Pattern CHAT_TIMESTAMP_PREFIX = Pattern.compile("^\\[[0-2]\\d:[0-5]\\d:[0-5]\\d]\\s+");

    @ModifyVariable(
        method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private Text onAddMessage(Text original) {
        if (original == null) return null;
        
        // Remove Discord warning suffix from the original message text
        String originalString = original.getString();
        if (originalString.contains("Please be mindful of Discord links")) {
            originalString = DISCORD_WARNING_SUFFIX.matcher(originalString).replaceAll("");
            original = Text.literal(originalString);
        }

        // 1. Texte brut sans codes couleur
        String raw = COLOR_CODE.matcher(original.getString()).replaceAll("");
        raw = raw.replaceAll("\\s+", " ").trim();

        // Remove leading chat timestamp like [22:00:25]
        raw = CHAT_TIMESTAMP_PREFIX.matcher(raw).replaceFirst("");

        // Remove the Discord security warning suffix that Hypixel appends (additional safety check)
        raw = DISCORD_WARNING_SUFFIX.matcher(raw).replaceAll("");
        raw = raw.trim();

        // 2. Seulement les messages de guild / officer (longues formes ou abrégées)
        if (!raw.startsWith("Guild > ") && !raw.startsWith("Officer > ") 
            && !raw.startsWith("G > ") && !raw.startsWith("O > ")) return original;

        // 3. Test du pattern bridge
        Matcher m = BRIDGE_HEADER.matcher(raw);
        if (!m.matches()) return original; // message guild normal → on ne touche à rien

        // 4. Extraction des groupes
        String headerChannel = m.group(1); // Guild ou Officer
        String botMC = m.group(3); // ex: "KetroX"
        String payload = m.group(5); // ex: "D > MeteoFrance: test"

        // 5. Vérification du bot : si botMCName est défini, on ne formate QUE ce bot
        BridgeConfig cfg = BridgeConfig.get();
        if (cfg.botMCName != null && !cfg.botMCName.equalsIgnoreCase(botMC)) {
            return original; // ce n'est pas notre bot → on ne touche à rien
        }
        String channelMarker = extractChannelMarker(payload);
        boolean hasVersionTag = hasGuildVersionTag(payload);
        boolean isBridgePayload = hasChannelMarker(payload) || hasVersionTag;
        boolean isOfficerChat = "Officer".equalsIgnoreCase(headerChannel)
            || "O".equalsIgnoreCase(channelMarker);
        if (!isBridgePayload && !cfg.formatAllGuild) {
            return original; // pas de marqueur canal/version et mode guilde inactif → on ne touche à rien
        }

        String prefix = resolvePrefix(cfg, isOfficerChat);
        String prefixColorCode = cfg.randomMode
            ? randomColorCode()
            : safeColorCode(isOfficerChat ? cfg.officerPrefixColor : cfg.guildPrefixColor);

        // 6. Nettoyage du payload pour supporter plusieurs formats (V1/V2/V3)
        if (!isBridgePayload) {
            String message = payload.trim();
            if (message.isEmpty()) return original;
            String playerColorCode = cfg.randomMode
                ? randomColorCode()
                : safeColorCode(cfg.discordNameColor);
            String formatted = "§" + prefixColorCode + prefix + "§8 > "
                + "§" + playerColorCode + botMC
                + "§8: §f" + message;
            return Text.literal(formatted);
        }
        String cleaned = stripLeadingNonVersionTag(payload);

        String guildVersion = null;
        Matcher versionMatcher = GUILD_VERSION_TAG.matcher(cleaned);
        if (versionMatcher.find()) {
            guildVersion = versionMatcher.group(1).toUpperCase();
            cleaned = cleaned.substring(versionMatcher.end()).trim();
        }

        cleaned = stripLeadingNonVersionTag(cleaned);

        String markerToStrip = extractChannelMarker(cleaned);
        if (markerToStrip != null && cleaned.startsWith(markerToStrip + " > ")) {
            cleaned = cleaned.substring(markerToStrip.length() + 3);
        }

        String discord;
        String message;
        int sepIndex = cleaned.indexOf(": ");
        if (sepIndex >= 0) {
            discord = cleaned.substring(0, sepIndex).trim();
            message = cleaned.substring(sepIndex + 2).trim();
        } else {
            sepIndex = cleaned.indexOf(" > ");
            if (sepIndex < 0) return original;
            discord = cleaned.substring(0, sepIndex).trim();
            message = cleaned.substring(sepIndex + 3).trim();
        }
        if (discord.isEmpty() || message.isEmpty()) return original;

        // 7. Construction du message formaté
        //    Format : "G > v2 > PseudoDiscord : message" (sinon fallback alias, ex: Bridge)
        String aliasColorCode = cfg.randomMode
            ? randomColorCode()
            : safeColorCode(cfg.botAliasColor);
        String playerColorCode = cfg.randomMode
            ? randomColorCode()
            : safeColorCode(cfg.discordNameColor);

        boolean useVersion = cfg.versionFormattingEnabled && guildVersion != null && !guildVersion.isBlank();
        String versionOrAlias = useVersion ? guildVersion : cfg.botAlias;
        String versionOrAliasColorCode = cfg.randomMode
            ? randomColorCode()
            : resolveVersionOrAliasColorCode(cfg, versionOrAlias, useVersion, aliasColorCode);

        String formatted = "§" + prefixColorCode + prefix + "§8 > "
            + "§" + versionOrAliasColorCode + versionOrAlias
            + " §8> "
            + "§" + playerColorCode + discord
            + "§8 : §f" + message;

        return Text.literal(formatted);
    }

    @Unique
    private static String safeColorCode(String code) {
        if (code == null || code.isEmpty()) return "b";
        return code.substring(0, 1).toLowerCase();
    }

    @Unique
    private static String randomColorCode() {
        return RANDOM_COLORS[ThreadLocalRandom.current().nextInt(RANDOM_COLORS.length)];
    }

    @Unique
    private static String extractChannelMarker(String payload) {
        if (payload == null || payload.isEmpty()) return null;
        String cleaned = stripLeadingNonVersionTag(payload);
        Matcher marker = CHANNEL_MARKER.matcher(cleaned);
        return marker.matches() ? marker.group(1) : null;
    }

    @Unique
    private static String resolvePrefix(BridgeConfig cfg, boolean isOfficerChat) {
        String prefix = isOfficerChat ? cfg.officerPrefix : cfg.guildPrefix;
        return (prefix == null || prefix.isBlank()) ? (isOfficerChat ? "O" : "G") : prefix.trim();
    }

    @Unique
    private static boolean hasChannelMarker(String payload) {
        if (payload == null || payload.isEmpty()) return false;
        String cleaned = stripLeadingNonVersionTag(payload);
        if (cleaned.startsWith("[")) {
            int end = cleaned.indexOf("] ");
            if (end > 0 && end + 2 <= cleaned.length()) {
                String after = cleaned.substring(end + 2);
                if (after.contains(": ")) return true;
                cleaned = after;
            }
        }
        return CHANNEL_MARKER.matcher(cleaned).matches();
    }

    @Unique
    private static boolean hasGuildVersionTag(String payload) {
        if (payload == null || payload.isEmpty()) return false;
        String cleaned = stripLeadingNonVersionTag(payload);
        return GUILD_VERSION_TAG.matcher(cleaned).find();
    }

    @Unique
    private static String stripLeadingNonVersionTag(String payload) {
        if (payload == null || payload.isEmpty()) return payload;
        if (!payload.startsWith("[")) return payload;
        int end = payload.indexOf("] ");
        if (end <= 0 || end + 2 > payload.length()) return payload;
        String tag = payload.substring(1, end);
        if ("V1".equalsIgnoreCase(tag) || "V2".equalsIgnoreCase(tag) || "V3".equalsIgnoreCase(tag)) {
            return payload;
        }
        return payload.substring(end + 2);
    }

    @Unique
    private static String resolveVersionOrAliasColorCode(BridgeConfig cfg, String versionOrAlias, boolean useVersion, String aliasColorCode) {
        if (!useVersion) return aliasColorCode;
        if ("V1".equalsIgnoreCase(versionOrAlias)) return safeColorCode(cfg.guildVersionV1Color);
        if ("V2".equalsIgnoreCase(versionOrAlias)) return safeColorCode(cfg.guildVersionV2Color);
        if ("V3".equalsIgnoreCase(versionOrAlias)) return safeColorCode(cfg.guildVersionV3Color);
        return aliasColorCode;
    }
}