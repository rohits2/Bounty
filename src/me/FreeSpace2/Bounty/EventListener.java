package me.FreeSpace2.Bounty;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventListener implements Listener{
	Configuration config;
	Economy econ;
	EventListener(Configuration config, Economy econ){
		this.config=config;
		this.econ=econ;
	}
	@EventHandler
	public void onPlayerLoginEvent(PlayerLoginEvent event){
		Player player=event.getPlayer();
		if(!config.getConfigurationSection("bounties").contains(event.getPlayer().getDisplayName())){
			config.set("bounties."+event.getPlayer().getDisplayName(), 0);
		}
		if(config.getInt("bounties."+player.getDisplayName())>0){
			player.sendMessage("There is a bounty of "+config.getInt("bounties."+player.getDisplayName())+" against you!");
		}
	}
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event){
		if(event.getEntity() instanceof Player){
			Player victim=(Player)event.getEntity();
			EntityDamageEvent cause=victim.getLastDamageCause();
			if(cause.getCause()==EntityDamageEvent.DamageCause.ENTITY_ATTACK){
				Entity killer=((EntityDamageByEntityEvent)victim.getLastDamageCause()).getDamager();
				if(killer instanceof Player){
					if(config.getInt("bounties."+victim.getDisplayName())>0)
					event.setDeathMessage(event.getDeathMessage()+". "+((Player) killer).getDisplayName()+" also got the bounty, "+config.getInt("bounties."+victim.getDisplayName())+"!");
					econ.bankDeposit(((Player) killer).getDisplayName(), config.getInt("bounties."+victim.getDisplayName()));
				}
			}
		}
	}
}
