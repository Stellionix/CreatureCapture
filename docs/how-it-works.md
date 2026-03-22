## CreatureCapture - How It Works

CreatureCapture revolves around a special bow called the Capture Bow.

### Capture Bow acquisition

- The Capture Bow can appear as a special enchantment-table result on a bow.
- The chance is controlled by `droprate`.
- The special offer only appears on a high-level enchantment roll.

### Capture flow

1. A player shoots with a Capture Bow.
2. The fired projectile is tagged as a capture arrow.
3. If the arrow hits a valid entity, CreatureCapture rolls the configured capture chance.
4. On success, the target is removed and a spawn egg is dropped.
5. The creature is stored in the player's collection if it is a new unique capture.

### Durability behavior

- The Capture Bow uses custom durability shown in lore such as `10/10`.
- Each shot reduces the displayed durability by `1`.
- If `captureBowDurability` is `-1`, the lore displays `∞` and the bow never breaks through this system.

### Supported creatures

- By default, all alive and spawnable entity types are enabled in `Mob_eggs`.
- Iron Golems are handled specially through a custom egg item.
- If a creature does not have a usable spawn egg material, it cannot be captured.

### Progress tracking

- Captures are stored per player UUID.
- Duplicate captures do not increase unique collection progress.
- `/cc collection`, `/cc stats`, `/cc remaining`, and `/cc top` all read from the same persistent capture store.
