package fr.swansky.bankxp.commands;

import org.bukkit.entity.Player;

import java.util.UUID;

public class UserBankXP {
    private final UUID playerUUID;
    private double xpCount;

    public UserBankXP(UUID playerUUID, double xpCount) {
        this.playerUUID = playerUUID;
        this.xpCount = xpCount;
    }

    public UserBankXP(Player player) {
        this.playerUUID = player.getUniqueId();
        this.xpCount = 0;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public double getXpPoint() {
        return xpCount;
    }

    public void setXpCount(long xpCount) {
        this.xpCount = xpCount;
    }

    public void addXp(float exp) {
        this.xpCount += exp;
    }

    public void removeXP(float exp) {
        this.xpCount -= exp;
    }
}
