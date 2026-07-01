<div align="center">

# 📂 GuildZip 📂

### Your Hypixel guild chat, finally readable.

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2-brightgreen)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Fabric-0.19.3-orange)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](./LICENSE)

<img width="265" height="61" alt="preview" src="https://github.com/user-attachments/assets/39af7b73-32f2-4ae7-962c-0e8b86914a77" />

</div>

---

GuildZip reformats your Hypixel **guild chat** into clean, compact lines — and it recognizes Discord **bridge** relays to rewrite those too.

## 👀 Before → after

| Hypixel sends | GuildZip shows |
|---|---|
| `Guild > [MVP++] PlayerName [RAT]: hello` | `G > PlayerName : hello` |
| `Guild > [MVP++] BotName: G > DiscordUser: hey` | `G > Bridge > DiscordUser : hey` |
| `Guild > [MVP++] BotName: [V2] G > DiscordUser: gg` | `G > V2 > DiscordUser : gg` |

Everything's tunable in `/gz` — full RGB colors, prefixes, and per-guild version labels. `/gz update` checks for a new version.

Needs **Fabric API** + **[Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)**. Cloth Config is optional, for the settings screen.

---

## ⚡ TL;DR — bridging several guilds

<div align="center">

Need the bridge itself? → **[Minecraft-Bridge-Chat](https://github.com/FrenchLegacy/Minecraft-Bridge-Chat)**

Running one Discord bridge across multiple guilds? Have the bot tag each guild's messages with `[V1]` / `[V2]` / `[V3]`, set **Bot MC Name** to the bridge account in `/gz`, and every guild lands in your chat as its own colored, labeled line.


---

<div align="center">

Made with ❤️ by **[Tjiba](https://github.com/Tjiba)** • MeteoFrance in-game  
rejoins notre guilde : [FrenchLegacy](https://frenchlegacy.fr)

**[⬇ Download](https://github.com/Tjiba/GuildZip/releases)** &nbsp;·&nbsp; **[🐛 Report a bug](https://github.com/Tjiba/GuildZip/issues)** &nbsp;·&nbsp; **[📋 Changelog](https://github.com/Tjiba/GuildZip/blob/master/CHANGELOG.md)**

<br>

*⭐ If GuildZip helps you, a review goes a long way!*

</div>
