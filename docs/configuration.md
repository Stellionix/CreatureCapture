## CreatureCapture - Configuration

Make sure you have [installed](installation.md) the plugin before editing the configuration.

Edit `plugins/CreatureCapture/config.yml`, then run `/cc reload`.

### Global settings

| Key                          | Type    | Default | Description |
|-----------------------------|---------|---------|-------------|
| `droprate`                  | integer | `3`     | Chance in percent for the Capture Bow enchantment offer to appear in the enchantment table. |
| `captureBowDurability`      | integer | `10`    | Number of shots available on a Capture Bow. Use `-1` for unlimited durability. |
| `chanceToCapture`           | number  | `50`    | Chance in percent for a valid capture attempt to succeed. |
| `spawnersCanBeModifiedByEgg`| boolean | `true`  | Allow players to use spawn eggs on spawners. When `false`, right-clicking a spawner with an egg is blocked. |

### Creature availability

| Key        | Type                   | Default | Description |
|------------|------------------------|---------|-------------|
| `Mob_eggs` | map<string, boolean>   | Generated automatically | Per-creature toggle that controls whether a creature can be captured and converted into a spawn egg. |

Behavior details:

- The plugin generates `Mob_eggs` automatically for all alive and spawnable entity types.
- Missing entries are added automatically when the plugin detects new entity types.
- Older configs using top-level entity keys are converted into the `Mob_eggs` section.

### Notes

- After changing config values, run `/cc reload`.
- Setting a creature to `false` in `Mob_eggs` disables capturing that entity.
- Iron Golem support is special-cased and uses a custom-named spawn egg item.
