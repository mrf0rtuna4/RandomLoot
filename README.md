# RandomLoot

[![Paper 1.21.11](https://img.shields.io/badge/Paper-1.21.11-2ea043?logo=minecraft&logoColor=white)](#requirements)
[![Java 21](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)](#requirements)
[![Version](https://img.shields.io/badge/version-0.1.0-blue)](./CHANGELOG.md)
[![License: ARR](https://img.shields.io/badge/license-ARR-red)](./LICENSE)
[![Discord](https://img.shields.io/badge/Discord-Heypers-5865F2?logo=discord&logoColor=white)](https://discord.gg/N8MYbANVJ6)

> RandomLoot is a lightweight Paper (Purpur) plugin that replaces default block drops with unpredictable loot and optional event effects.

<img width="1366" height="745" alt="Example 1" src="https://github.com/user-attachments/assets/7d3f279b-e20a-4f4c-b154-2d37ae53cf3b" />


RandomLoot was recently used during a special Heypers 5th anniversary event, and is now officially released as a public repository.

## Features

- Random loot on block break with configurable amount ranges.
- Optional random mob spawn per block break.
- Special events support (explosion, potion effects, particles, lightning).
- Block blacklist / whitelist logic.
- Excluded material list for loot filtering.
- Admin command set for toggles, status, and reload.

## Requirements

- Paper: 1.21.11+
- Java: 21+

## Installation

1. Build the plugin:
      ./gradlew build
   
2. Put the built .jar from build/libs/ into your server plugins/ folder.
3. Start or restart the server.
4. Edit plugins/RandomLoot/config.yml if needed.
5. Use /randomloot help.

## Commands

- /randomloot help
- /randomloot status
- /randomloot reload
- /randomloot toggle <block|mob|event|spawn>

## Permissions

- randomloot.command — base command access.
- randomloot.admin — admin management commands.

## Configuration highlights

config.yml allows tuning:

- block and mob loot toggles;
- spawn/special-event chances;
- loot count and stack size ranges;
- blocked/allowed block lists;
- excluded materials.

## Support

- Website: https://www.heypers.org/
- Discord: https://discord.gg/N8MYbANVJ6 

## License

This repository is distributed under ARR (All Rights Reserved).
See [LICENSE](./LICENSE) for details.
