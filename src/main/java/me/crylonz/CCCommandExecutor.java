package me.crylonz;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static me.crylonz.CreatureCapture.generateCaptureBow;

public class CCCommandExecutor implements CommandExecutor, TabExecutor {

    private final CreatureCapture plugin;

    public CCCommandExecutor(CreatureCapture p) {
        this.plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player) || !cmd.getName().equalsIgnoreCase("cc")) {
            return true;
        }

        if (args.length < 1) {
            return true;
        }

        if (args[0].equalsIgnoreCase("get") && canUseGetCommand(
                player.hasPermission("creaturecapture.cc"),
                player.hasPermission("creaturecapture.get")
        )) {
            player.getInventory().addItem(generateCaptureBow(new ItemStack(Material.BOW)));
        }

        if (args[0].equalsIgnoreCase("reload") && player.hasPermission("creaturecapture.reload")) {
            player.sendMessage("reload config....");
            plugin.reloadConfig();
            plugin.loadConfig();
            player.sendMessage("config reloaded.");
        }

        if (args[0].equalsIgnoreCase("collection") && canUseCollectionCommand(
                player.hasPermission("creaturecapture.cc"),
                player.hasPermission("creaturecapture.collection")
        )) {
            sendCollectionMessage(player);
        }

        if (args[0].equalsIgnoreCase("stats") && canUseStatsCommand(
                player.hasPermission("creaturecapture.cc"),
                player.hasPermission("creaturecapture.stats")
        )) {
            sendStatsMessage(player);
        }

        if (args[0].equalsIgnoreCase("top") && canUseTopCommand(
                player.hasPermission("creaturecapture.cc"),
                player.hasPermission("creaturecapture.top")
        )) {
            sendTopMessage(player);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1) {
            tabs.addAll(buildTabCompletions(
                    canUseGetCommand(sender.hasPermission("creaturecapture.cc"), sender.hasPermission("creaturecapture.get")),
                    sender.hasPermission("creaturecapture.reload"),
                    canUseCollectionCommand(sender.hasPermission("creaturecapture.cc"), sender.hasPermission("creaturecapture.collection")),
                    canUseStatsCommand(sender.hasPermission("creaturecapture.cc"), sender.hasPermission("creaturecapture.stats")),
                    canUseTopCommand(sender.hasPermission("creaturecapture.cc"), sender.hasPermission("creaturecapture.top"))
            ));
        }
        return tabs;
    }

    static boolean canUseGetCommand(boolean hasCcPermission, boolean hasGetPermission) {
        return hasCcPermission || hasGetPermission;
    }

    static boolean canUseCollectionCommand(boolean hasCcPermission, boolean hasCollectionPermission) {
        return hasCcPermission || hasCollectionPermission;
    }

    static boolean canUseStatsCommand(boolean hasCcPermission, boolean hasStatsPermission) {
        return hasCcPermission || hasStatsPermission;
    }

    static boolean canUseTopCommand(boolean hasCcPermission, boolean hasTopPermission) {
        return hasCcPermission || hasTopPermission;
    }

    static List<String> buildTabCompletions(boolean canGet, boolean canReload, boolean canCollection, boolean canStats, boolean canTop) {
        List<String> tabs = new ArrayList<>();
        if (canGet) {
            tabs.add("get");
        }
        if (canReload) {
            tabs.add("reload");
        }
        if (canCollection) {
            tabs.add("collection");
        }
        if (canStats) {
            tabs.add("stats");
        }
        if (canTop) {
            tabs.add("top");
        }
        return tabs;
    }

    private void sendCollectionMessage(Player player) {
        Set<String> capturedCreatures = plugin.getCapturedCreatures(player.getUniqueId());
        int totalCreatures = CreatureCapture.getCollectibleEntityTypes().size();
        int progress = CreatureCapture.calculateCollectionProgress(capturedCreatures.size(), totalCreatures);

        player.sendMessage(ChatColor.GOLD + "Creature Collection");
        player.sendMessage(ChatColor.YELLOW + "Progress: " + ChatColor.WHITE + capturedCreatures.size() + "/" + totalCreatures + " (" + progress + "%)");

        if (capturedCreatures.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "You have not captured any creatures yet.");
            return;
        }

        String captures = capturedCreatures.stream()
                .map(CreatureCapture::formatEntityTypeName)
                .collect(Collectors.joining(ChatColor.DARK_GRAY + ", " + ChatColor.GREEN, ChatColor.GREEN.toString(), ""));

        player.sendMessage(ChatColor.YELLOW + "Captured: " + captures);
    }

    private void sendStatsMessage(Player player) {
        PlayerCaptureStats stats = plugin.getPlayerStats(player.getUniqueId());
        int totalCreatures = CreatureCapture.getCollectibleEntityTypes().size();
        int progress = CreatureCapture.calculateCollectionProgress(stats.uniqueCaptureCount(), totalCreatures);

        player.sendMessage(ChatColor.GOLD + "Creature Stats");
        player.sendMessage(ChatColor.YELLOW + "Unique captures: " + ChatColor.WHITE + stats.uniqueCaptureCount());
        player.sendMessage(ChatColor.YELLOW + "Completion: " + ChatColor.WHITE + progress + "%");
    }

    private void sendTopMessage(Player player) {
        List<LeaderboardEntry> leaderboard = plugin.getTopPlayers(10);

        player.sendMessage(ChatColor.GOLD + "Creature Top 10");
        if (leaderboard.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "No captures have been recorded yet.");
            return;
        }

        for (int i = 0; i < leaderboard.size(); i++) {
            LeaderboardEntry entry = leaderboard.get(i);
            String playerName = entry.playerName() == null || entry.playerName().isBlank()
                    ? entry.playerId().toString()
                    : entry.playerName();
            player.sendMessage(ChatColor.YELLOW + "" + (i + 1) + ". " + ChatColor.WHITE + playerName + ChatColor.DARK_GRAY + " - " + ChatColor.GREEN + entry.captureCount());
        }
    }
}
