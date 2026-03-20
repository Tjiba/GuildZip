# Changelog

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