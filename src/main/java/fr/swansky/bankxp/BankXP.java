package fr.swansky.bankxp;

import fr.swansky.bankxp.commands.BankXPCommand;
import fr.swansky.bankxp.config.YMLConfigBankXP;
import org.bukkit.plugin.java.JavaPlugin;


public final class BankXP extends JavaPlugin {
    private YMLConfigBankXP ymlConfigBankXP;

    @Override
    public void onEnable() {
        ymlConfigBankXP = new YMLConfigBankXP();
        // Plugin startup logic
        getCommand("xpbank").setExecutor(new BankXPCommand(ymlConfigBankXP));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public YMLConfigBankXP getYmlConfigBankXP() {
        return ymlConfigBankXP;
    }
}
