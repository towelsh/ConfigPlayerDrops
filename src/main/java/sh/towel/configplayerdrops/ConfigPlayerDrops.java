package sh.towel.configplayerdrops;

import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigPlayerDrops extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(getConfig()), this);
    }
}
