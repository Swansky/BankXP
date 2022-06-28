package fr.swansky.bankxp.multiParamCommand;

import java.util.ArrayList;
import java.util.List;

public class VariableParam<T> {
    private final String name;
    private final Class<T> variableType;
    private T defaultValue;
    private final List<Validator<T>> validators = new ArrayList<>();

    public VariableParam(String name, Class<T> variableType) {
        this.name = name;
        this.variableType = variableType;
    }

    public VariableParam(String name, Class<T> variableType, T defaultValue) {
        this(name, variableType);
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public Class<?> getVariableType() {
        return variableType;
    }

    public String getVariableDescription() {
        return "<" + name + ">";
    }

    public VariableParam<T> addValidator(Validator<T> validator) {
        validators.add(validator);
        return this;
    }

    public List<Validator<T>> getValidators() {
        return validators;
    }

    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}
