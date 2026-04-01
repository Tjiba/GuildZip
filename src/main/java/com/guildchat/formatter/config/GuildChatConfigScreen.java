package com.guildchat.formatter.config;

import com.guildchat.formatter.BridgeConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.Arrays;

public class GuildChatConfigScreen {
    
    private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();

    private enum ChatColorOption {
        BLACK("0", "Black"),
        DARK_BLUE("1", "Dark Blue"),
        DARK_GREEN("2", "Dark Green"),
        DARK_CYAN("3", "Dark Cyan"),
        DARK_RED("4", "Dark Red"),
        PURPLE("5", "Purple"),
        ORANGE("6", "Orange"),
        LIGHT_GRAY("7", "Light Gray"),
        DARK_GRAY("8", "Dark Gray"),
        LIGHT_BLUE("9", "Light Blue"),
        LIGHT_GREEN("a", "Light Green"),
        AQUA("b", "Aqua"),
        RED("c", "Red"),
        MAGENTA("d", "Magenta"),
        YELLOW("e", "Yellow"),
        WHITE("f", "White");

        private final String code;
        private final String displayName;

        ChatColorOption(String code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            // Return minecraft color code + display name for colored display in enum selector
            return "§" + code + displayName;
        }

        public static ChatColorOption fromCode(String code) {
            if (code != null) {
                for (ChatColorOption option : values()) {
                    if (option.code.equalsIgnoreCase(code)) {
                        return option;
                    }
                }
            }
            return WHITE;
        }
    }
    
    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("GuildZip"))
                .setSavingRunnable(() -> {
                    BridgeConfig.get().save();
                    BridgeConfig.reload();
                });

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigCategory colors = builder.getOrCreateCategory(Text.literal("Colors"));
        ConfigCategory advanced = builder.getOrCreateCategory(Text.literal("Advanced"));

        // ═══════════════════════════════════════════════════════════════════════════════
        // GENERAL CATEGORY
        // ═══════════════════════════════════════════════════════════════════════════════
        
        general.addEntry(ENTRY_BUILDER
                .startStrField(Text.literal("Bot MC Name"), BridgeConfig.get().botMCName != null ? BridgeConfig.get().botMCName : "")
                .setDefaultValue("")
                .setSaveConsumer(value -> BridgeConfig.get().botMCName = value.isEmpty() ? null : value)
                .setTooltip(Text.literal("Minecraft name of the Discord bot (leave empty for auto-detection)"))
                .build());

        general.addEntry(ENTRY_BUILDER
                .startStrField(Text.literal("Bridge Alias"), BridgeConfig.get().botAlias)
                .setDefaultValue("Bridge")
                .setSaveConsumer(value -> BridgeConfig.get().botAlias = value.isEmpty() ? "Bridge" : value)
                .setTooltip(Text.literal("Name to display instead of the bot's Minecraft name"))
                .build());

        general.addEntry(ENTRY_BUILDER
                .startBooleanToggle(Text.literal("Format All Guild Messages"), BridgeConfig.get().formatAllGuild)
                .setDefaultValue(false)
                .setSaveConsumer(value -> BridgeConfig.get().formatAllGuild = value)
                .setTooltip(Text.literal("Enable formatting for all guild messages, not just Discord bridge"))
                .build());

        general.addEntry(ENTRY_BUILDER
                .startStrField(Text.literal("Guild Prefix"), BridgeConfig.get().guildPrefix)
                .setDefaultValue("G")
                .setSaveConsumer(value -> BridgeConfig.get().guildPrefix = value.isBlank() ? "G" : value)
                .setTooltip(Text.literal("Prefix for guild chat (shown as prefix>)"))
                .build());

        general.addEntry(ENTRY_BUILDER
                .startStrField(Text.literal("Officer Prefix"), BridgeConfig.get().officerPrefix)
                .setDefaultValue("O")
                .setSaveConsumer(value -> BridgeConfig.get().officerPrefix = value.isBlank() ? "O" : value)
                .setTooltip(Text.literal("Prefix for officer chat (shown as prefix>)"))
                .build());

        general.addEntry(ENTRY_BUILDER
                .startBooleanToggle(Text.literal("Version Formatting (v1/v2/v3)"), BridgeConfig.get().versionFormattingEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(value -> BridgeConfig.get().versionFormattingEnabled = value)
                .setTooltip(Text.literal("Display guild version label instead of Bridge alias when [V1]/[V2]/[V3] is detected"))
                .build());

        // ═══════════════════════════════════════════════════════════════════════════════
        // COLORS CATEGORY
        // ═══════════════════════════════════════════════════════════════════════════════
        
        ChatColorOption currentAliasColor = ChatColorOption.fromCode(BridgeConfig.get().botAliasColor);
        
        colors.addEntry(ENTRY_BUILDER
                .startEnumSelector(
                        Text.literal("Bridge Alias Color"),
                        ChatColorOption.class,
                        currentAliasColor
                )
                .setDefaultValue(ChatColorOption.AQUA)
                .setSaveConsumer(value -> BridgeConfig.get().botAliasColor = value.getCode())
                .setTooltip(Text.literal("Click to cycle through colors"))
                .build());

        ChatColorOption currentPlayerColor = ChatColorOption.fromCode(BridgeConfig.get().discordNameColor);
        
        colors.addEntry(ENTRY_BUILDER
                .startEnumSelector(
                        Text.literal("Player Color"),
                        ChatColorOption.class,
                        currentPlayerColor
                )
                .setDefaultValue(ChatColorOption.DARK_CYAN)
                .setSaveConsumer(value -> BridgeConfig.get().discordNameColor = value.getCode())
                .setTooltip(Text.literal("Color for player names in messages"))
                .build());

        ChatColorOption currentGuildPrefixColor = ChatColorOption.fromCode(BridgeConfig.get().guildPrefixColor);

        colors.addEntry(ENTRY_BUILDER
                .startEnumSelector(
                        Text.literal("Guild Prefix Color"),
                        ChatColorOption.class,
                        currentGuildPrefixColor
                )
                .setDefaultValue(ChatColorOption.LIGHT_GREEN)
                .setSaveConsumer(value -> BridgeConfig.get().guildPrefixColor = value.getCode())
                .setTooltip(Text.literal("Click to cycle through colors"))
                .build());

        ChatColorOption currentOfficerPrefixColor = ChatColorOption.fromCode(BridgeConfig.get().officerPrefixColor);

        colors.addEntry(ENTRY_BUILDER
                .startEnumSelector(
                        Text.literal("Officer Prefix Color"),
                        ChatColorOption.class,
                        currentOfficerPrefixColor
                )
                .setDefaultValue(ChatColorOption.MAGENTA)
                .setSaveConsumer(value -> BridgeConfig.get().officerPrefixColor = value.getCode())
                .setTooltip(Text.literal("Use random colors for each message (overrides the colors above)"))
                .build());

        // Version Colors Section
        ChatColorOption currentV1Color = ChatColorOption.fromCode(BridgeConfig.get().guildVersionV1Color);
        colors.addEntry(ENTRY_BUILDER
                .startEnumSelector(
                        Text.literal("Version V1 Color"),
                        ChatColorOption.class,
                        currentV1Color
                )
                .setDefaultValue(ChatColorOption.LIGHT_GREEN)
                .setSaveConsumer(value -> BridgeConfig.get().guildVersionV1Color = value.getCode())
                .setTooltip(Text.literal("Color used for the v1 label"))
                .build());

        ChatColorOption currentV2Color = ChatColorOption.fromCode(BridgeConfig.get().guildVersionV2Color);
        colors.addEntry(ENTRY_BUILDER
                .startEnumSelector(
                        Text.literal("Version V2 Color"),
                        ChatColorOption.class,
                        currentV2Color
                )
                .setDefaultValue(ChatColorOption.YELLOW)
                .setSaveConsumer(value -> BridgeConfig.get().guildVersionV2Color = value.getCode())
                .setTooltip(Text.literal("Color used for the v2 label"))
                .build());

        ChatColorOption currentV3Color = ChatColorOption.fromCode(BridgeConfig.get().guildVersionV3Color);
        colors.addEntry(ENTRY_BUILDER
                .startEnumSelector(
                        Text.literal("Version V3 Color"),
                        ChatColorOption.class,
                        currentV3Color
                )
                .setDefaultValue(ChatColorOption.RED)
                .setSaveConsumer(value -> BridgeConfig.get().guildVersionV3Color = value.getCode())
                .setTooltip(Text.literal("Color used for the v3 label"))
                .build());

        colors.addEntry(ENTRY_BUILDER
                .startBooleanToggle(Text.literal("Random Colors"), BridgeConfig.get().randomMode)
                .setDefaultValue(false)
                .setSaveConsumer(value -> BridgeConfig.get().randomMode = value)
                .setTooltip(Text.literal("Use random colors for each message (overrides the colors above)"))
                .build());

        // ═══════════════════════════════════════════════════════════════════════════════
        // ADVANCED CATEGORY
        // ═══════════════════════════════════════════════════════════════════════════════
        
        advanced.addEntry(ENTRY_BUILDER
                .startStringDropdownMenu(
                        Text.literal("Language"),
                        BridgeConfig.get().language,
                        Text::literal
                )
                .setSelections(Arrays.asList("english", "french"))
                .setDefaultValue("english")
                .setSaveConsumer(value -> BridgeConfig.get().language = value)
                .setTooltip(Text.literal("Interface language (english / french)"))
                .build());

        advanced.addEntry(ENTRY_BUILDER
                .startBooleanToggle(Text.literal("Enable Auto-Updater"), BridgeConfig.get().autoUpdaterEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(value -> BridgeConfig.get().autoUpdaterEnabled = value)
                .setTooltip(Text.literal("Automatically download the latest release jar into the mods folder"))
                .build());

        return builder.build();
    }
}