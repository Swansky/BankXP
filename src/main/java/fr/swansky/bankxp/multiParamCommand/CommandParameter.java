package fr.swansky.bankxp.multiParamCommand;

import java.util.*;

public class CommandParameter {
    private final Map<String, ArgumentParam> argumentParams = new HashMap<>();


    public ArgumentParam registerArgument(String name, UserCommandCallBack callBack, String permission) {
        ArgumentParam argumentParam = new ArgumentParam(name, callBack, permission);
        argumentParams.put(name, argumentParam);
        return argumentParam;
    }

    public List<String> getArgumentsName() {
        return new ArrayList<>(argumentParams.keySet());
    }


    public Optional<ArgumentParam> getArgumentValueAtPosition(String argName) {
        return Optional.ofNullable(argumentParams.get(argName));
    }

    public Optional<ArgumentParam> getArgumentByName(String arg) {
        return Optional.ofNullable(argumentParams.get(arg));
    }

    public Collection<ArgumentParam> getArguments() {
        return argumentParams.values();
    }
}
