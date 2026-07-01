# Changelog

## 1.6.0 - 2026-07-01
- ⬆️ **Updated to Minecraft 26.2**
- 🎨 **Full RGB colors** — every element (prefixes, alias, names, version labels) is now a color picker instead of the 16 Minecraft color codes
- 🧭 **Reworked config screen** — cleaner categories (General / Colors / Version tags); `/gz` now opens the settings directly and no longer requires Mod Menu (Cloth Config is enough)
- 🌍 **English only** — removed the French translation and the language option
- 🧑‍💻 **Rewritten in Kotlin** (the chat mixin stays in Java) — leaner codebase, same behavior; now requires **Fabric Language Kotlin**
- 🧹 Internal: package renamed to `com.guildzip`, chat parsing extracted to a testable formatter, dead code removed

## 1.5.0 - 2026-06-12
- ⬆️ **Ported to Minecraft 26.1.x** (26.1 is unobfuscated: migrated from Yarn to official Mojang names)
- ☕ Now requires Java 25 (Minecraft 26.1 requirement)
- 🔧 Updated toolchain: Gradle 9.5.1, Loom 1.17.11 (`net.fabricmc.fabric-loom`), Fabric Loader 0.19.3, Fabric API 0.151.0
- 🔧 Updated dependencies: Mod Menu 18.0.0-beta.1, Cloth Config 26.1.154
- 🛠️ Chat mixin retargeted to the new `ChatComponent#addMessage` (still covers player, system and server messages)
- 🗑️ Removed duplicate message compaction `(x2)` — Hypixel already blocks sending the same message twice
- 🔔 **Removed the auto-updater** (no more automatic jar downloads) — replaced by a simple clickable chat notification ~5 s after joining Hypixel; `/gz dismiss` and `/gz noupdate` removed, toggle available in config (Advanced → Update Notifications)
- 🛡️ Update check now only looks at mod versions published for the running Minecraft version

## 1.3.1 - 2026-03-31
- 🤖 Added auto-updater: checks latest GitHub release and can auto-download the JAR into `mods/`
- ⚙️ Added `Enable Auto-Updater` toggle in Mod Menu (Advanced)
- 💬 Added `/gcs update` and `/guildchatshortener update` commands for manual trigger
- 🧹 Reworked update flow to async callbacks (removed sleep-based notifier)

## 1.3.0 - 2026-03-20
- ✨ V1/V2/V3 labels display correctly even with a custom bridge alias 
- ⚙️ Config access via Mod Menu (Cloth Config required)
- ✅ New shortcuts: `/gcs` and `/guildchatshortener` to open config
- 🧹 Command list trimmed to `/bridge status` plus config shortcuts
- 📚 Documentation cleaned for the release

## 1.2.4 - 2026-02-18
- 🔥 **MAJOR**: Version is now read dynamically from mod metadata
- 🐛 **FIX**: No more version synchronization issues between code and gradle.properties
- ✨ CURRENT_VERSION is now automatically synced with fabric.mod.json
- 🎯 Eliminates false "update available" messages when already on latest version
- 🧹 Removed hardcoded version string from VersionManager.java

## 1.2.3 - 2026-02-18
- 🔄 Silent automatic version checks (only shows messages for updates or dev versions)
- 💬 Added dev version detection message
- 🐛 Fixed version comparison logic
- 🧹 Code cleanup (removed unused methods and warnings)

## 1.2.2 - 2026-02-18
- ✨ **NEW**: `/bridge update` command to manually check for updates
- 🔄 Improved version checking system with dynamic version display
- 🐛 **FIX**: Version comparison now correctly identifies development versions
- 📊 Better error handling for version checks (connection issues, timeouts)
- 💬 Dynamic update messages showing current and latest versions
- 🧹 Added version cache management (reset, check status)
- 📚 New documentation: VERSION_CHECK.md, UPDATE_GUIDE.md
- 🌍 Updated all help messages to include `/bridge update`
- 🎯 Improved UPDATE_AVAILABLE message to show both versions
- ⚡ Smarter waiting mechanism for manual version checks
- 🔧 Enhanced VersionManager with new utility methods
- ✨ Added support for development version detection

## 1.2.1 - 2026-02-18
- 🐛 Removed unused imports and fields
- 🐛 Fixed deprecated URL handling (Java 20+)
- 🐛 Corrected EnvType comparison for environment detection
- 🐛 Removed obsolete HTML attributes from README files
- 🐛 Cleaned up all compiler warnings
- 🔧 Optimized version comparison logic
- ✨ Improved code stability

## 1.2.0 - 2026-02-18
- ✨ Complete ModMenu integration - Access settings from ModMenu
- 🎨 Graphical configuration menu with Cloth Config
- 🌈 Color dropdown menu with colorized preview (Red, Blue, Green, etc.)
- 🖼️ Mod icon support (icon.png displayed in ModMenu)
- 🔧 Refactored color management system with readable names
- 🐛 Fixed file duplication issues during compilation
- 📝 Updated mod name everywhere (GuildChat Shortener)
- 🌐 Added Modrinth link to mod information

## 1.1.0 - 2026-02-17
- Added multilingual system (English/French)
- New /bridgelanguage command to change language
- All mod messages now available in English and French
- French is the default language

## 1.0.0 - 2026-02-17
- Added /bridgeactivateall to format normal guild messages
- Improved Discord bridge message detection (roles and formats)
- Simplified /bridge status and shows active mode