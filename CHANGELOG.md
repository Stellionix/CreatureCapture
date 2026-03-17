# Changelog

## 2.4.0 - 2026-03-17

- Fixed capture bow handling so captures are attributed to the actual shooter instead of a nearby player.
- Fixed capture reliability for entities such as Allay, Breeze and Enderman.
- Added `/cc remaining` to list the creatures a player still needs to capture.
- Added the `creaturecapture.remaining` permission.
- Added console support for `/cc reload`.
- Added server log output when `/cc reload` is executed.

## 2.3.0 - 2026-03-12

- Added a collection system that tracks every unique creature you capture.
- Added `/cc collection` to view your captured creatures and overall completion progress.
- Added `/cc stats` to quickly check your personal collection progress.
- Added `/cc top` to view the server leaderboard for unique creature captures.
- Player collections are now saved permanently, so progress is kept between restarts.
- Improved support for newer Minecraft server versions while keeping compatibility with older creature naming differences.
- Fixed mooshroom capture handling across API naming changes.
- Improved Iron Golem egg handling to make special captures more reliable.

## 2.2.0 - 2024-01-29

- Added official support for 1.20.x.
- Updated commands:
- `/cc get` to get the capture bow.
- `/cc reload` to reload the plugin.
- Improved the config file structure by moving egg configuration under `Mob_eggs`.
- Added `captureBowDurability = -1` support for infinite arrows.
- Thanks to BrokenArrow for the contribution.

## 2.1.0 - 2023-07-23

- Added support for all entities on 1.18+.
- Fixed mooshroom capture not working.

## 2.0 - 2022-03-27

- Added support for 1.17.x.
- Added support for 1.18.x.
- All mobs are now supported.
- Fixed the duplicate Iron Golem egg issue.

## 1.12 - 2021-04-13

- Added Iron Golem capture.
- Added Iron Golem egg.
- All entities are now supported.
- Code rewrite.
- Optimization work.

## 1.11 - 2020-10-17

- Added Piglin egg drop.
- Added Zoglin egg drop.
- Added Strider egg drop.
- Added Hoglin egg drop.

## 1.10 - 2020-07-09

- Fixed a bow event exception.
- Added official support for 1.16.

## 1.9 - 2020-06-23

- Fixed a bug with the bow when shooting mobs.

## 1.8 - 2020-03-22

- Added a config option to enable or disable putting eggs into spawners.
- Added a config option to choose per mob whether it can be captured.

## 1.7 - 2019-12-11

- Added Bee support.

## 1.6 - 2019-10-29

- Added support for 1.12.

## 1.5 - 2019-08-26

- Patched an issue where an enchanted capture bow did not work.

## 1.4 - 2019-08-04

- Added fixed bow durability, configurable in `config.yml`.
- Added configurable capture chance, configurable in `config.yml`.
- Improved server-side balancing and customization.

## 1.3 - 2019-06-02

- Fixed incorrect egg drops for several creatures.

## 1.2 - 2019-05-28

- Added support for 1.13.2.
- Fixed wrong eggs for Villager and Phantom.
- Added a configuration option for the enchant-table appearance chance of the capture bow.
- Patched capture bow enchanting so bookshelves are required again, with a minimum level of 15.
- Added the `/cc` command to give yourself the capture bow.

## 1.1 - 2019-05-27

- Added `creaturecapture.capture` permission to allow players to capture creatures.
- Various bug fixes and optimizations.

## 1.0 - 2019-05-26

- Initial version.
