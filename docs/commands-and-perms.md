## CreatureCapture - Commands & Permissions

Make sure you have [installed](installation.md) the plugin before reading this section.

This page documents the current `/cc` commands and permission nodes.

### Commands

| Command            | Permission                    | Description |
|-------------------|-------------------------------|-------------|
| `/cc get`         | `creaturecapture.get`         | Give yourself a Capture Bow. |
| `/cc reload`      | `creaturecapture.reload`      | Reload `config.yml`. |
| `/cc collection`  | `creaturecapture.collection`  | Show your captured creatures and collection progress. |
| `/cc stats`       | `creaturecapture.stats`       | Show your number of unique captures and completion percentage. |
| `/cc remaining`   | `creaturecapture.remaining`   | Show the creatures you still need to capture. |
| `/cc top`         | `creaturecapture.top`         | Show the top players by unique captures. |

### Permission Nodes

| Permission                      | Description |
|---------------------------------|-------------|
| `creaturecapture.capture`       | Allows capture attempts with capture arrows. |
| `creaturecapture.cc`            | Grants broad access to player `/cc` subcommands. |
| `creaturecapture.get`           | Allows `/cc get`. |
| `creaturecapture.reload`        | Allows `/cc reload`. |
| `creaturecapture.collection`    | Allows `/cc collection`. |
| `creaturecapture.stats`         | Allows `/cc stats`. |
| `creaturecapture.remaining`     | Allows `/cc remaining`. |
| `creaturecapture.top`           | Allows `/cc top`. |

### Permission behavior

- `/cc reload` checks only `creaturecapture.reload`.
- Player-facing subcommands accept either the specific permission node or the broader `creaturecapture.cc` node.
- `creaturecapture.capture` controls whether a player-fired capture arrow can actually capture a mob.

### Tab completion

Tab completion only suggests subcommands the sender is allowed to use.
