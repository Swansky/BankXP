package fr.swansky.bankxp.multiParamCommand;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface UserCommandCallBack {

    boolean onUserCommand(Player player, Command command, @NotNull String label, @NotNull List<Object> args);
}
