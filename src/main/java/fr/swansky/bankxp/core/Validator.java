package fr.swansky.bankxp.core;

import org.bukkit.entity.Player;

public interface Validator<T> {
    void validate(T valueToValidate, Player executor, String parameterName) throws ValidatorException;
}
