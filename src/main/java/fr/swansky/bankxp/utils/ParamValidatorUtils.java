package fr.swansky.bankxp.utils;

import fr.swansky.bankxp.multiParamCommand.ValidatorException;
import org.bukkit.entity.Player;


public class ParamValidatorUtils {
    public static void ExecutorCannotByTheSame(Player variable, Player executor, String parameterName) throws ValidatorException {
        if (variable.getUniqueId().equals(executor.getUniqueId())) {
            throw new ValidatorException("You cannot select yourself for parameter %s", parameterName);
        }
    }

    public static void CannotBeSubOne(Number variable, Player executor, String parameterName) throws ValidatorException {
        if (variable.doubleValue() < 1) {
            throw new ValidatorException("Cannot be less than 1 for parameter %s", parameterName);
        }
    }
}
