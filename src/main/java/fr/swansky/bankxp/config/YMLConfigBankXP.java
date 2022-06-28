package fr.swansky.bankxp.config;


import fr.swansky.bankxp.commands.UserBankXP;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class YMLConfigBankXP extends YMLConfig<UUID, UserBankXP> {

    public YMLConfigBankXP() {
        super("bankXP");
        read();
    }

    @Override
    public void read() {
        ConfigurationSection banksSection = config.getConfigurationSection("xpbank");
        if (banksSection == null) return;
        UUID playerUUID;
        double balance;
        UserBankXP wallet;
        ConfigurationSection walletSection;
        for (String key : banksSection.getKeys(false)) {
            walletSection = banksSection.getConfigurationSection(key);
            playerUUID = UUID.fromString(key);
            balance = walletSection.getDouble("balance");
            wallet = new UserBankXP(playerUUID, balance);
            items.put(playerUUID, wallet);
        }

    }

    @Override
    public void write(UserBankXP userBankXP) {
        items.put(userBankXP.getPlayerUUID(), userBankXP);
        System.out.println(userBankXP.getPlayerUUID());
        System.out.println(userBankXP.getXpPoint());
        ConfigurationSection xpbank = config.getConfigurationSection("xpbank");
        if (xpbank == null) {
            xpbank = config.createSection("xpbank");
        }
        ConfigurationSection section = xpbank.createSection(userBankXP.getPlayerUUID().toString());
        section.set("balance", userBankXP.getXpPoint());
    }


    public void writeWithSave(UserBankXP userBankXP) {
        write(userBankXP);
        save();
    }
}
