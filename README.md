<h1  align="center">
    <img src="creature-capture-logo.png" alt="Creature-Capture" width="800" /><br>
</h1>

<h2  align="center">
    <img src="http://cf.way2muchnoise.eu/full_322752_downloads.svg" alt="download"/> 
    <img src="https://github.com/stellionix/CreatureCapture/actions/workflows/ci.yml/badge.svg" alt="CI"/>
    <a href="https://stellionix.github.io/CreatureCapture/"><img src="https://img.shields.io/badge/docs-online-blue" alt="Docs"/></a>
    <img src="https://img.shields.io/github/license/stellionix/creaturecapture" alt="licence"/>
    <img src="https://img.shields.io/github/last-commit/stellionix/creaturecapture" alt="commit"/>
</h2>


**Catch them all !**

Java Edition required. Creature Capture is mainly compatible with Bukkit, Spigot and Paper.

## Features

- Capture compatible creatures with a custom Capture Bow.
- Add the Capture Bow as a special enchantment outcome in the enchantment table.
- Configure which mobs can be captured through `config.yml`.
- Support custom Iron Golem spawn egg handling.
- Track each player's unique captures.
- Persist collection data in SQLite with automatic migration from the old `captures.yml` format.
- Show collection progress with `/cc collection`.
- Show personal completion stats with `/cc stats`.
- Show the server leaderboard with `/cc top`.
- Tab completion for all plugin subcommands.


## Commands

- `/cc get`: gives the player a Capture Bow.
- `/cc reload`: reloads the plugin configuration.
- `/cc collection`: displays captured creatures and completion progress.
- `/cc stats`: displays the player's unique capture count and completion percentage.
- `/cc top`: displays the top players by unique captures.

## Permissions

- `creaturecapture.capture`: allows creature capture.
- `creaturecapture.cc`: grants access to all base `/cc` features.
- `creaturecapture.get`: allows `/cc get`.
- `creaturecapture.reload`: allows `/cc reload`.
- `creaturecapture.collection`: allows `/cc collection`.
- `creaturecapture.stats`: allows `/cc stats`.
- `creaturecapture.top`: allows `/cc top`.


## Download and Info

* Curseforge: https://www.curseforge.com/minecraft/bukkit-plugins/creaturecapture
* Bukkit : https://dev.bukkit.org/projects/creaturecapture

