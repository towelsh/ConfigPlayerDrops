package sh.towel.configplayerdrops;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerDeathListener implements Listener {
    private ICombatLogX combatLogX;
    private FileConfiguration config;

    public PlayerDeathListener(FileConfiguration config) {
        this.config = config;
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.isPluginEnabled("CombatLogX")) {
            Bukkit.getLogger().info("Hooked into CombatLogX");
            this.combatLogX = (ICombatLogX) pluginManager.getPlugin("CombatLogX");
        }
    }

    private boolean dropItems(PlayerDeathEvent e) {
        // combat logged
        if (this.combatLogX != null) {
            if (this.combatLogX.getCombatManager().isInCombat(e.getPlayer())) {
                return config.getBoolean("combat_logged");
            }
        }

        if (e.getPlayer().getKiller() == null) {
            // not directly killed by player
            System.out.println("LDC: " + e.getPlayer().getLastDamageCause().getCause());
            System.out.println("LDC lower: " + e.getPlayer().getLastDamageCause().getCause().toString().toLowerCase());
            System.out.println("CONFIG : " + config.getBoolean(e.getPlayer().getLastDamageCause().getCause().toString().toLowerCase()));
            return config.getBoolean(e.getPlayer().getLastDamageCause().getCause().toString().toLowerCase(), config.getBoolean("other"));
        }  else {
            return config.getBoolean("player");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (dropItems(e)) {
            e.setKeepInventory(false);
        } else {
            e.setKeepInventory(true);
            e.getDrops().clear();
        }
    }
}
