package fr.ethan.eternia;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {
	private HashMap<Player, Integer> cooldownTime;
	private HashMap<Player, BukkitRunnable> cooldownTask;
   
	public void onEnable() {
		this.cooldownTime = new HashMap<>();
		this.cooldownTask = new HashMap<>();
	}
	
	public void insulteAleat(Player target, Player p) {
		
		String insultes[] = { 
				"Espèce de koungouz des montages ! ",
				"Commence par me parler poliment ",
				"Espèce de sexe d'oursin c:"
				};
		
		int max = insultes.length;
		int min = 0;
		int aleat = min + (int)(Math.random() * ((max - min) + 1));
		
		
		target.sendMessage(ChatColor.YELLOW + insultes[aleat] + ChatColor.RED+ "(" + p.getName() + ')');
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Seul les joueurs peuvent insulter <3");
			return true;
		} 
		final Player p = (Player)sender;
		if (commandLabel.equalsIgnoreCase("insulte")) {
			if (args.length != 1) {
				p.sendMessage(ChatColor.RED + "utilisation: /" + commandLabel + " <Pseudo>");
				return true;
			} 
			Player target = p.getServer().getPlayer(args[0]);
			if (target == null) {
				p.sendMessage(ChatColor.YELLOW + " Impossible de trouver le joueur " + ChatColor.RED + args[0]);
				return true;
			} 
			if (target.getName() == p.getName()) {
				p.sendMessage(ChatColor.YELLOW + "Juste, tu comptes vraiment t'insulter toi même ?");
				return true;
			} 
   
			if (args.length == 1) {
				if (this.cooldownTime.containsKey(p)) {
					p.sendMessage(ChatColor.YELLOW + "Tu dois attendre " + ChatColor.RED + this.cooldownTime.get(p) + ChatColor.YELLOW + " secondes avant d'insulter quelqu'un.");
					return true;
				} 
				p.sendMessage(ChatColor.YELLOW + "Tu as insulté " + ChatColor.RED + target.getName());
				insulteAleat(target,p);
				
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_INFECT, 0);
				this.cooldownTime.put(p, Integer.valueOf(30));
				this.cooldownTask.put(p, new BukkitRunnable() {
					public void run() {
						Main.this.cooldownTime.put(p, Integer.valueOf(((Integer)Main.this.cooldownTime.get(p)).intValue() - 1));
						if (((Integer)Main.this.cooldownTime.get(p)).intValue() == 0) {
							Main.this.cooldownTime.remove(p);
							Main.this.cooldownTask.remove(p);
							cancel();
						} 
					}
				});
				((BukkitRunnable)this.cooldownTask.get(p)).runTaskTimer((Plugin)this, 20L, 20L);
				return true;
			} 
		} 
		return false;
	}
}