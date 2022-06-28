package fr.swansky.bankxp.multiParamCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class UserConfigurableCommand extends UserCommand implements TabCompleter {
    private final CommandParameter parameter;

    public UserConfigurableCommand() {
        this.parameter = defineConfig();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public boolean onUserCommand(Player player, Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            Optional<ArgumentParam> argumentOptional = parameter.getArgumentByName(args[0]);
            if (argumentOptional.isPresent()) {
                ArgumentParam argumentParam = argumentOptional.get();
                if (argumentParam.getPermission().isEmpty() || player.hasPermission(argumentParam.getPermission())) {
                    if (argumentParam.getNumberOfVariablesNeeded() <= args.length) {
                        int pos = 0;
                        List<Object> valuesParse = new ArrayList<>();
                        for (String arg : args) {
                            if (pos > 0 && pos < argumentParam.getNumberOfVariablesNeeded()) {
                                VariableParam<?> variableParam = argumentParam.getVariableAtPosition(pos - 1);
                                try {
                                    Object valueParse = parseStringTo(arg, variableParam.getVariableType());
                                    valuesParse.add(valueParse);
                                    for (Validator validator : variableParam.getValidators()) {
                                        validator.validate(valueParse, player, variableParam.getName());
                                    }
                                } catch (IllegalArgumentException e) {
                                    player.sendMessage(String.format("The value enter for parameter '%s' is not valid.", variableParam.getName()));
                                    return true;
                                } catch (ValidatorException e) {
                                    player.sendMessage(e.getMessage());
                                    return true;
                                }
                            }
                            pos++;
                        }

                        return argumentParam.getCallBack().onUserCommand(player, command, label, valuesParse);
                    } else {
                        player.sendMessage("Invalid command missing parameters.");
                    }
                } else {
                    player.sendMessage("You don't have the permission to execute this command.");
                }

            } else {
                player.sendMessage("This argument is not valid");
            }
        }
        return true;
    }


    public Object parseStringTo(String valueToParse, Class<?> type) throws IllegalArgumentException {
        if (type.isEnum()) {
            for (Object enumConstant : type.getEnumConstants()) {
                if (enumConstant.toString().equalsIgnoreCase(valueToParse)) {
                    return enumConstant;
                }
            }
            throw new IllegalArgumentException("Invalid enum value.");
        } else if (type.isAssignableFrom(Float.class)) {
            return Float.parseFloat(valueToParse);
        } else if (type.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(valueToParse);
        } else if (type.isAssignableFrom(Long.class)) {
            return Long.parseLong(valueToParse);
        } else if (type.isAssignableFrom(Short.class)) {
            return Short.parseShort(valueToParse);
        } else if (type.isAssignableFrom(Player.class)) {
            Player player = Bukkit.getPlayer(valueToParse);
            if (player != null) {
                return player;
            } else {
                throw new IllegalArgumentException("Invalid player name.");
            }
        }
        throw new IllegalArgumentException(String.format("Impossible to parse this class: '%s'", type.getName()));
    }

    @NotNull
    public abstract CommandParameter defineConfig();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> values = new ArrayList<>();
        if (args.length <= 1) {
            Collection<ArgumentParam> arguments = parameter.getArguments();
            for (ArgumentParam argument : arguments) {
                if (argument.getPermission().isEmpty() || sender.hasPermission(argument.getPermission()))
                    values.add(argument.getName());
            }
        } else {
            Optional<ArgumentParam> argumentValueAtPosition = parameter.getArgumentValueAtPosition(args[0]);
            if (argumentValueAtPosition.isPresent()) {
                ArgumentParam argumentParam = argumentValueAtPosition.get();

                List<VariableParam<?>> variables = argumentParam.getVariables();
                int pos = args.length - 2;
                if (variables.size() > pos) {
                    VariableParam<?> variableParam = variables.get(pos);
                    if (variableParam.getVariableType().isAssignableFrom(Player.class)) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            values.add(onlinePlayer.getName());
                        }
                    } else if (variableParam.getVariableType().isEnum()) {
                        for (Object enumConstant : variableParam.getVariableType().getEnumConstants()) {
                            values.add(enumConstant.toString().toLowerCase());
                        }
                    } else {
                        values.add(variableParam.getName());
                    }
                }
            }
        }
        return values;
    }
}
