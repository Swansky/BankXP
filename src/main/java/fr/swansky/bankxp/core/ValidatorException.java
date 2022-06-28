package fr.swansky.bankxp.core;

public class ValidatorException extends Exception {
    public ValidatorException(String message, Object... arg) {
        super(String.format(message, arg));
    }
}
