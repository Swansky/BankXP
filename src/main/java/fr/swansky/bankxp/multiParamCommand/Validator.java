package fr.swansky.bankxp.multiParamCommand;

import org.bukkit.entity.Player;

public interface Validator<T> {
    void validate(T valueToValidate, Player executor, String parameterName) throws ValidatorException;
}
