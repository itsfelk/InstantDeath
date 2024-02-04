package its.felk.instantdeath;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class InstantDeath extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("[InstantDeath] Made with love by felk");
        getLogger().info("[InstantDeath] Trans lives matter! :3");
    }

    @Override
    public void onDisable() {
        getLogger().info("[InstantDeath] Thanks for using Instant Death <3");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            // Handle the command when executed from the console
            handleConsoleCommand(sender, label, args);
            return true;
        }

        Player player = (Player) sender;

        // Check if the command is "kill" or its alias "suicide" and has valid arguments
        if ((label.equalsIgnoreCase("kill") || label.equalsIgnoreCase("suicide")) && args.length <= 1) {
            // Handle self-kill or targeted kill based on the provided arguments
            if (args.length == 0) {
                handleKill(player, true); // Self-kill
            } else if (hasKillPermission(player)) {
                Player targetPlayer = getServer().getPlayer(args[0]);
                if (targetPlayer != null) {
                    handleKill(targetPlayer, false); // Targeted kill
                } else {
                    player.sendMessage("Player " + args[0] + " not found.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission to kill others.");
            }
            return true;
        }

        // Send usage message for invalid command or arguments
        player.sendMessage(ChatColor.RED + "Usage: /" + label + " [player]");
        return true;
    }

    // Handle the kill command when executed from the console
    private void handleConsoleCommand(CommandSender sender, String label, String[] args) {
        if ((label.equalsIgnoreCase("kill") || label.equalsIgnoreCase("suicide")) && args.length == 1) {
            Player targetPlayer = getServer().getPlayer(args[0]);

            if (targetPlayer != null) {
                // Kill the target player from the console
                targetPlayer.setHealth(0.0);
                getLogger().info("Player " + targetPlayer.getName() + " killed from console.");
            } else {
                getLogger().info("Player " + args[0] + " not found.");
            }
        } else {
            getLogger().info("Usage from console: /" + label + " <player>");
        }
    }

    // Format coordinates for display
    private String formatCoordinates(int x, int y, int z) {
        return ChatColor.YELLOW + "X: " + ChatColor.GOLD + x +
                ChatColor.RED + ", " +
                ChatColor.YELLOW + "Y: " + ChatColor.GOLD + y +
                ChatColor.RED + ", " +
                ChatColor.YELLOW + "Z: " + ChatColor.GOLD + z;
    }

    // Handle the kill command for both self-kill and targeted kill
    private void handleKill(Player target, boolean selfKill) {
        int x = target.getLocation().getBlockX();
        int y = target.getLocation().getBlockY();
        int z = target.getLocation().getBlockZ();

        // Kill the target player
        target.setHealth(0.0);

        // Send appropriate kill message to the executor
        if (selfKill) {
            target.sendMessage(ChatColor.RED + "You killed yourself at " + formatCoordinates(x, y, z));
        } else {
            target.sendMessage(ChatColor.RED + "You killed " + target.getName() + "!");
        }
    }

    // Check if the player has the permission to kill others
    private boolean hasKillPermission(Player player) {
        return player.hasPermission("minecraft.command.kill") || player.isOp();
    }
}