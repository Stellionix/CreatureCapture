package me.crylonz;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static me.crylonz.CreatureCapture.generateCaptureBow;

public class CCCommandExecutor implements CommandExecutor, TabExecutor {

    private final CreatureCapture plugin;

    public CCCommandExecutor(CreatureCapture p) {
        this.plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player;
        if ((sender instanceof Player)) {
            player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("cc")) {
                if (args.length < 1) return true;

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
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1) {
            tabs.addAll(buildTabCompletions(
                    canUseGetCommand(sender.hasPermission("creaturecapture.cc"), sender.hasPermission("creaturecapture.get")),
                    sender.hasPermission("creaturecapture.reload")
            ));
        }
        return tabs;
    }

    static boolean canUseGetCommand(boolean hasCcPermission, boolean hasGetPermission) {
        return hasCcPermission || hasGetPermission;
    }

    static List<String> buildTabCompletions(boolean canGet, boolean canReload) {
        List<String> tabs = new ArrayList<>();
        if (canGet) {
            tabs.add("get");
        }
        if (canReload) {
            tabs.add("reload");
        }
        return tabs;
    }
}
