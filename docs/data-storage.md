## CreatureCapture - Data Storage

CreatureCapture stores collection progress in the plugin data folder.

### Current storage

- Database file: `plugins/CreatureCapture/captures.db`
- Engine: SQLite
- Key model: one unique entry per player UUID and entity type

This means repeated captures of the same creature do not create duplicate collection entries.

### Stored information

- Player UUID
- Latest known player name
- Entity type name

