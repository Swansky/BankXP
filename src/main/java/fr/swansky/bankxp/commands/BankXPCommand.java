package fr.swansky.bankxp.commands;

import fr.swansky.bankxp.config.YMLConfigBankXP;
import fr.swansky.bankxp.core.ArgumentParam;
import fr.swansky.bankxp.core.CommandParameter;
import fr.swansky.bankxp.core.UserConfigurableCommand;
import fr.swansky.bankxp.utils.MessageUtils;
import fr.swansky.bankxp.utils.ParamValidatorUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class BankXPCommand extends UserConfigurableCommand {
    public final YMLConfigBankXP ymlConfigBankXP;

    public static final Class<Integer> XP_AMOUNT_TYPE = Integer.class;

    public BankXPCommand(YMLConfigBankXP ymlConfigBankXP) {
        this.ymlConfigBankXP = ymlConfigBankXP;
    }

    private boolean balanceXP(Player player, Command command, @NotNull String s, @NotNull List<Object> objects) {
        UserBankXP bank = getBankOfUser(player);
        player.sendMessage(
                MessageUtils.InfoMessage(
                        String.format("actual balance %s levels (%s points)", (int) getLevelFromExp(bank.getXpPoint()), bank.getXpPoint())));
        return true;
    }

    private boolean transferXP(Player player, Command command, @NotNull String s, List<Object> args) {
        int amount = (int) args.get(0);
        Player playerToTransfer = (Player) args.get(1);

        UserBankXP ownerBank = getBankOfUser(player);

        if (amount > ownerBank.getXpPoint()) {
            player.sendMessage(MessageUtils.InfoMessage("You don't have enough xp point ! "));
        } else {
            UserBankXP receiverBank = getBankOfUser(playerToTransfer);
            ownerBank.removeXP(amount);
            receiverBank.addXp(amount);
            ymlConfigBankXP.write(ownerBank);
            ymlConfigBankXP.write(receiverBank);
            ymlConfigBankXP.save();
            player.sendMessage(MessageUtils.InfoMessage(String.format("You has transferred %s xp points", amount)));
            playerToTransfer.sendMessage(MessageUtils.InfoMessage(String.format("You has received %s xp points", amount)));
        }

        return true;
    }

    private boolean depositXP(Player player, Command command, @NotNull String s, List<Object> args) {
        int numberPointToDeposit = (int) args.get(0);

        UserBankXP bank = getBankOfUser(player);

        if (numberPointToDeposit > player.getTotalExperience()) {
            player.sendMessage(MessageUtils.InfoMessage("You don't have enough xp level."));
        } else {
            bank.addXp(numberPointToDeposit);
            ymlConfigBankXP.writeWithSave(bank);
            player.giveExp(-numberPointToDeposit);
            player.sendMessage(MessageUtils.InfoMessage("Your xp level has been deposited."));
            player.sendMessage(MessageUtils.InfoMessage(String.format("actual balance %s levels (%s points)", (int) getLevelFromExp(bank.getXpPoint()), bank.getXpPoint())));
        }
        return true;
    }

    private boolean withdrawXP(Player player, Command command, @NotNull String s, List<Object> args) {
        int numberPointToWithDraw = (int) args.get(0);

        UserBankXP bank = getBankOfUser(player);

        if (numberPointToWithDraw > bank.getXpPoint()) {
            player.sendMessage(MessageUtils.InfoMessage("You don't have enough xp level in your bank."));
        } else {

            bank.removeXP(numberPointToWithDraw);
            ymlConfigBankXP.writeWithSave(bank);
            player.giveExp(numberPointToWithDraw);
            player.sendMessage(MessageUtils.InfoMessage("Your xp level have been added."));
            player.sendMessage(MessageUtils.InfoMessage(String.format("actual balance %s (%s points)", (int) getLevelFromExp(bank.getXpPoint()), bank.getXpPoint())));
        }
        return true;
    }

    public boolean addXP(Player player, Command command, @NotNull String label, @NotNull List<Object> args) {
        int amountLevelToAdd = (int) args.get(0);
        Player playerToAdd = (Player) args.get(1);
        UserBankXP bank = getBankOfUser(playerToAdd);


        int expToNextLevel = getExpToNextLevel((int) getLevelFromExp(bank.getXpPoint()), amountLevelToAdd);
        bank.addXp(expToNextLevel);
        ymlConfigBankXP.writeWithSave(bank);
        player.sendMessage(MessageUtils.InfoMessage(String.format("XP have been added to player %s ", playerToAdd.getName())));
        playerToAdd.sendMessage(MessageUtils.InfoMessage(String.format("Your xp bank has been credited by %s for %s levels", player.getName(), amountLevelToAdd)));
        return true;
    }


    private UserBankXP getBankOfUser(Player player) {
        Optional<UserBankXP> userBankOptional = ymlConfigBankXP.getByID(player.getUniqueId());
        return userBankOptional.orElseGet(() -> new UserBankXP(player));
    }

    public double getLevelFromExp(double exp) {
        if (exp > 1395) {
            return (Math.sqrt(72 * exp - 54215) + 325) / 18;
        }
        if (exp > 315) {
            return Math.sqrt(40 * exp - 7839) / 10 + 8.1;
        }
        if (exp > 0) {
            return Math.sqrt(exp + 9) - 3;
        }
        return 0;
    }

    private int getExpToNextLevel(int actualLevel) {
        if (actualLevel > 30) {
            return 9 * actualLevel - 158;
        }
        if (actualLevel > 15) {
            return 5 * actualLevel - 38;
        }
        return 2 * actualLevel + 7;
    }

    private int getExpToNextLevel(int actualLevel, int levelAdded) {
        int totalExp = 0;
        for (int i = 0; i < levelAdded; i++) {
            totalExp += getExpToNextLevel(actualLevel + i);
        }
        return totalExp;
    }

    @Override
    public @NotNull CommandParameter defineConfig() {
        CommandParameter parameter = new CommandParameter();
        ArgumentParam argumentParam = parameter.registerArgument("add", this::addXP, "bankxp.add");
        argumentParam.addOrderVariable("amount", XP_AMOUNT_TYPE)
                .addValidator(ParamValidatorUtils::CannotBeSubOne);
        argumentParam.addOrderVariable("player", Player.class);

        ArgumentParam argumentParamTransfer = parameter.registerArgument("transfer", this::transferXP, "");
        argumentParamTransfer.addOrderVariable("amount", XP_AMOUNT_TYPE)
                .addValidator(ParamValidatorUtils::CannotBeSubOne);
        argumentParamTransfer.addOrderVariable("player", Player.class)
                .addValidator(ParamValidatorUtils::ExecutorCannotByTheSame);

        ArgumentParam argumentParamWithDraw = parameter.registerArgument("withdraw", this::withdrawXP, "");
        argumentParamWithDraw.addOrderVariable("amount", XP_AMOUNT_TYPE)
                .addValidator(ParamValidatorUtils::CannotBeSubOne);

        ArgumentParam argumentParamDeposit = parameter.registerArgument("deposit", this::depositXP, "");
        argumentParamDeposit.addOrderVariable("amount", XP_AMOUNT_TYPE)
                .addValidator(ParamValidatorUtils::CannotBeSubOne);

        parameter.registerArgument("balance", this::balanceXP, "");

        return parameter;
    }


}
