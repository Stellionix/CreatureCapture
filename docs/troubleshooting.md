## CreatureCapture - Troubleshooting

### The Capture Bow never appears in the enchantment table

- Check that `droprate` is greater than `0`.
- Make sure you are enchanting a plain bow.
- The special offer only appears on a high-level enchantment roll.

### Capturing does not work

- Confirm the player has `creaturecapture.capture`.
- Check that the target creature is enabled in `Mob_eggs`.
- Verify `chanceToCapture` is not set too low.
- Some entity types cannot be captured if no matching spawn egg material exists.

### Commands do nothing

- Check the relevant permission node.
- For player subcommands, `creaturecapture.cc` can also grant access.
- `/cc reload` is separate and requires `creaturecapture.reload`.

### Progress or leaderboard is empty

- Confirm the plugin was able to create `captures.db`.
- Check server logs for SQLite initialization errors.
- If you migrated from an old setup, verify whether `captures.yml` was renamed to `captures.yml.migrated`.

### Spawner interactions are blocked

- If `spawnersCanBeModifiedByEgg` is `false`, right-clicking spawners with eggs is intentionally blocked.
