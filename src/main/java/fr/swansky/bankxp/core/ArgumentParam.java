package fr.swansky.bankxp.core;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParam {
    private final String name;
    private final List<VariableParam<?>> variables = new ArrayList<>();

    private final UserCommandCallBack callBack;

    private final String permission;



    public ArgumentParam(String name, UserCommandCallBack callBack, String permission) {
        this.name = name;
        this.callBack = callBack;
        this.permission = permission;
    }

    public <T> VariableParam<T> addOrderVariable(String name, Class<T> typeOfParam) {
        VariableParam<T> variableParam = new VariableParam<T>(name, typeOfParam);
        variables.add(variableParam);
        return variableParam;
    }



    public UserCommandCallBack getCallBack() {
        return callBack;
    }

    public String getName() {
        return name;
    }


    public int getNumberOfVariablesNeeded() {
        return variables.size() + 1;
    }

    public VariableParam<?> getVariableAtPosition(int pos) {
        return variables.get(pos);
    }

    public String getPermission() {
        return permission;
    }

    public String getCommandDescription() {
        StringBuilder sb = new StringBuilder(name + " ");
        for (VariableParam<?> variable : variables) {
            sb.append(variable.getVariableDescription()).append(" ");
        }
        return sb.toString();
    }


    public List<VariableParam<?>> getVariables() {
        return variables;
    }
}
