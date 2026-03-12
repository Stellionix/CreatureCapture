package me.crylonz;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CaptureStoreTest {

    @TempDir
    Path tempDir;

    @Test
    void recordCaptureReturnsTrueOnlyForNewEntries() throws SQLException {
        UUID playerId = UUID.randomUUID();

        try (CaptureStore store = openStore()) {
            assertTrue(store.recordCapture(playerId, "Alice", EntityType.ZOMBIE));
            assertFalse(store.recordCapture(playerId, "Alice", EntityType.ZOMBIE));
        }
    }

    @Test
    void getCapturedCreaturesReturnsSortedUniqueValues() throws SQLException {
        UUID playerId = UUID.randomUUID();

        try (CaptureStore store = openStore()) {
            store.recordCapture(playerId, "Alice", EntityType.ZOMBIE);
            store.recordCapture(playerId, "Alice", EntityType.BLAZE);
            store.recordCapture(playerId, "Alice", EntityType.ZOMBIE);

            assertEquals(Set.of("BLAZE", "ZOMBIE"), store.getCapturedCreatures(playerId));
        }
    }

    @Test
    void getPlayerStatsReturnsUniqueCaptureCountAndLatestName() throws SQLException {
        UUID playerId = UUID.randomUUID();

        try (CaptureStore store = openStore()) {
            store.recordCapture(playerId, "Alice", EntityType.ZOMBIE);
            store.recordCapture(playerId, "AliceUpdated", EntityType.BLAZE);

            PlayerCaptureStats stats = store.getPlayerStats(playerId);
            assertEquals("AliceUpdated", stats.playerName());
            assertEquals(2, stats.uniqueCaptureCount());
        }
    }

    @Test
    void getTopPlayersReturnsLeaderboardSortedByCaptureCount() throws SQLException {
        UUID aliceId = UUID.randomUUID();
        UUID bobId = UUID.randomUUID();

        try (CaptureStore store = openStore()) {
            store.recordCapture(aliceId, "Alice", EntityType.ZOMBIE);
            store.recordCapture(aliceId, "Alice", EntityType.BLAZE);
            store.recordCapture(bobId, "Bob", EntityType.ZOMBIE);

            List<LeaderboardEntry> leaderboard = store.getTopPlayers(10);
            assertEquals(2, leaderboard.size());
            assertEquals("Alice", leaderboard.get(0).playerName());
            assertEquals(2, leaderboard.get(0).captureCount());
            assertEquals("Bob", leaderboard.get(1).playerName());
            assertEquals(1, leaderboard.get(1).captureCount());
        }
    }

    @Test
    void migrateLegacyCapturesImportsYamlAndRenamesFile() throws IOException, SQLException {
        UUID playerId = UUID.randomUUID();
        File legacyFile = tempDir.resolve("captures.yml").toFile();

        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("players." + playerId, List.of("ZOMBIE", "INVALID_TYPE", "BLAZE", "ZOMBIE"));
        yaml.save(legacyFile);

        try (CaptureStore store = openStore()) {
            assertEquals(Set.of("BLAZE", "ZOMBIE"), store.getCapturedCreatures(playerId));
            assertEquals(2, store.getPlayerStats(playerId).uniqueCaptureCount());
        }

        assertFalse(legacyFile.exists());
        assertTrue(tempDir.resolve("captures.yml.migrated").toFile().exists());
    }

    private CaptureStore openStore() throws SQLException {
        CaptureStore store = new CaptureStore(tempDir.toFile(), message -> { }, message -> { });
        store.open();
        return store;
    }
}
