package de.minestar.sixteenblocks.Listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class BaseListener implements Listener {

    // ////////////////////////////////////////////////
    //
    // ENTITY-LISTENER
    //
    // ////////////////////////////////////////////////

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    // ////////////////////////////////////////////////
    //
    // WORLD-LISTENER
    //
    // ////////////////////////////////////////////////

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        event.getWorld().setSpawnLocation(0, 6, 0);
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        event.getWorld().setSpawnLocation(0, 6, 0);
    }

    // ////////////////////////////////////////////////
    //
    // PLAYER-LISTENER
    //
    // ////////////////////////////////////////////////

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setGameMode(GameMode.CREATIVE);
        event.setJoinMessage("");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }

    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        event.setCancelled(true);
    }

    // ////////////////////////////////////////////////
    //
    // WEATHER-LISTENER
    //
    // ////////////////////////////////////////////////
    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if (event.toThunderState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    // ////////////////////////////////////////////////
    //
    // BLOCK-LISTENER
    //
    // ////////////////////////////////////////////////

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() != IgniteCause.FLINT_AND_STEEL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }
}
