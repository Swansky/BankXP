package fr.swansky.bankxp.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class UserCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            try {
                return onUserCommand(player, command, label, args);
            } catch (Exception e) {
                player.sendMessage("Impossible to execute this command.");
                e.printStackTrace();
                return true;
            }

        } else {
            sender.sendMessage("This command can execute only for a player.");
        }
        return true;
    }


    public abstract boolean onUserCommand(Player player, Command command, @NotNull String label, @NotNull String[] args);
}
