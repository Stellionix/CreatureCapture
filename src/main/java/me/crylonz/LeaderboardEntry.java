package me.crylonz;

import java.util.UUID;

record LeaderboardEntry(UUID playerId, String playerName, int captureCount) {
}
