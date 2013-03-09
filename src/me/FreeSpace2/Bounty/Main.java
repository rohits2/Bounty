package me.FreeSpace2.Bounty;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	Configuration config;
	Economy econ;
	public void onEnable(){
		initConfig();
		setupEconomy();
		this.getServer().getPluginManager().registerEvents(new EventListener(config, econ), this);
	}
	public void onDisable(){
		this.saveConfig();
	}
	private void initConfig(){
		config=this.getConfig();
		config.createSection("bounties");
		config.options().copyDefaults(true);
		this.saveConfig();
		config.getConfigurationSection("bounties").getKeys(false);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("bounty") && sender.hasPermission("Bounty.add")){
			try{
				config.set("bounties."+getServer().getOfflinePlayer(args[0]).getName(), config.getInt("bounties."+getServer().getOfflinePlayer(args[0]).getName())+Integer.parseInt(args[1]));
				econ.withdrawPlayer(sender.getName(), Integer.parseInt(args[1]));
			}catch(Exception e){
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	private boolean setupEconomy() {
        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}

