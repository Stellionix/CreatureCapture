package me.crylonz;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Consumer;

class CaptureStore implements AutoCloseable {

    private final File dataFolder;
    private final Consumer<String> warningLogger;
    private final Consumer<String> errorLogger;
    private Connection connection;

    CaptureStore(File dataFolder, Consumer<String> warningLogger, Consumer<String> errorLogger) {
        this.dataFolder = dataFolder;
        this.warningLogger = warningLogger;
        this.errorLogger = errorLogger;
    }

    void open() throws SQLException {
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            warningLogger.accept("Unable to create plugin data folder for captures.");
        }

        File databaseFile = new File(dataFolder, "captures.db");
        connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
        initializeDatabase();
        migrateLegacyCapturesIfNeeded();
    }

    boolean recordCapture(UUID playerId, String playerName, EntityType entityType) {
        if (connection == null) {
            return false;
        }

        String sql = "INSERT OR IGNORE INTO captures(player_uuid, player_name, entity_type) VALUES(?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerId.toString());
            statement.setString(2, playerName);
            statement.setString(3, entityType.name());
            boolean inserted = statement.executeUpdate() > 0;
            updatePlayerName(playerId, playerName);
            return inserted;
        } catch (SQLException e) {
            errorLogger.accept("Unable to record capture: " + e.getMessage());
            return false;
        }
    }

    Set<String> getCapturedCreatures(UUID playerId) {
        if (connection == null) {
            return Collections.emptySet();
        }

        String sql = "SELECT entity_type FROM captures WHERE player_uuid = ? ORDER BY entity_type ASC";
        Set<String> capturedCreatures = new TreeSet<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerId.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    capturedCreatures.add(resultSet.getString("entity_type"));
                }
            }
        } catch (SQLException e) {
            errorLogger.accept("Unable to load captures: " + e.getMessage());
        }

        return capturedCreatures;
    }

    PlayerCaptureStats getPlayerStats(UUID playerId) {
        if (connection == null) {
            return new PlayerCaptureStats("", 0);
        }

        String sql = "SELECT COALESCE(MAX(player_name), ''), COUNT(*) FROM captures WHERE player_uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerId.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new PlayerCaptureStats(resultSet.getString(1), resultSet.getInt(2));
                }
            }
        } catch (SQLException e) {
            errorLogger.accept("Unable to load player stats: " + e.getMessage());
        }

        return new PlayerCaptureStats("", 0);
    }

    List<LeaderboardEntry> getTopPlayers(int limit) {
        if (connection == null || limit <= 0) {
            return Collections.emptyList();
        }

        String sql = "SELECT player_uuid, COALESCE(MAX(player_name), ''), COUNT(*) AS capture_count " +
                "FROM captures GROUP BY player_uuid ORDER BY capture_count DESC, player_name ASC LIMIT ?";
        List<LeaderboardEntry> leaderboard = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    leaderboard.add(new LeaderboardEntry(
                            UUID.fromString(resultSet.getString(1)),
                            resultSet.getString(2),
                            resultSet.getInt(3)
                    ));
                }
            }
        } catch (SQLException e) {
            errorLogger.accept("Unable to load leaderboard: " + e.getMessage());
        }

        return leaderboard;
    }

    @Override
    public void close() {
        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            errorLogger.accept("Unable to close captures database: " + e.getMessage());
        } finally {
            connection = null;
        }
    }

    private void initializeDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS captures (" +
                            "player_uuid TEXT NOT NULL, " +
                            "player_name TEXT NOT NULL DEFAULT '', " +
                            "entity_type TEXT NOT NULL, " +
                            "PRIMARY KEY(player_uuid, entity_type)" +
                            ")"
            );
            try {
                statement.executeUpdate("ALTER TABLE captures ADD COLUMN player_name TEXT NOT NULL DEFAULT ''");
            } catch (SQLException ignored) {
                // Existing databases already have the column.
            }
        }
    }

    private void migrateLegacyCapturesIfNeeded() {
        File legacyCapturesFile = new File(dataFolder, "captures.yml");
        if (!legacyCapturesFile.exists()) {
            return;
        }

        YamlConfiguration legacyCaptures = YamlConfiguration.loadConfiguration(legacyCapturesFile);
        ConfigurationSection playersSection = legacyCaptures.getConfigurationSection("players");
        if (playersSection != null) {
            for (String playerId : playersSection.getKeys(false)) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(playerId);
                } catch (IllegalArgumentException ignored) {
                    continue;
                }

                for (String entityType : legacyCaptures.getStringList("players." + playerId)) {
                    EntityType parsedType = parseEntityType(entityType);
                    if (parsedType != null) {
                        recordCapture(uuid, "", parsedType);
                    }
                }
            }
        }

        File migratedCapturesFile = new File(dataFolder, "captures.yml.migrated");
        if (!legacyCapturesFile.renameTo(migratedCapturesFile)) {
            warningLogger.accept("Legacy captures.yml could not be renamed after migration.");
        }
    }

    private EntityType parseEntityType(String entityTypeName) {
        try {
            return EntityType.valueOf(entityTypeName);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private void updatePlayerName(UUID playerId, String playerName) throws SQLException {
        String sql = "UPDATE captures SET player_name = ? WHERE player_uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerName);
            statement.setString(2, playerId.toString());
            statement.executeUpdate();
        }
    }
}
