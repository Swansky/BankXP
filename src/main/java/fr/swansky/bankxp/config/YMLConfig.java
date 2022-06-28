package fr.swansky.bankxp.config;

import fr.swansky.bankxp.BankXP;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class YMLConfig<ID, T> {
    protected String filename;
    protected File file;
    protected YamlConfiguration config;

    private final BankXP plugin;

    protected final Map<ID, T> items = new HashMap<>();

    public YMLConfig(String filename) {
        this.filename = filename;
        this.plugin = BankXP.getPlugin(BankXP.class);
        this.file = new File(plugin.getDataFolder(), filename + ".yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        save();
    }

    public abstract void read();

    public abstract void write(T t);


    public void clean() {
        for (String key : this.config.getKeys(false)) {
            this.config.set(key, null);
        }
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            plugin.getLogger().warning("Unable to save yaml file : " + this.file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public Optional<T> getByID(ID id) {

        return Optional.ofNullable(items.get(id));
    }

}
