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

import net.md_5.bungee.api.chat.*;
//import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;

public class Main extends JavaPlugin {
	private HashMap<Player, Integer> cooldownTime;
	private HashMap<Player, BukkitRunnable> cooldownTask;
   
	public void onEnable() {
		this.cooldownTime = new HashMap<>();
		this.cooldownTask = new HashMap<>();
	}
	
	public void insulteAleat(Player target, Player p) {
		
		//insultes aléatoires
		String insultes[] = { 
				"Espèce de koungouz des montages ! ",
				"Commence par me parler poliment ",
				"Espèce de sexe d'oursin c: ",
				"Sale Clampin",
				"Va faire des tiktoks",
				"Sale Clampin",
				"Tu crois que c'est du respect ça mon garçon ?",
				"Va faire des tiktoks"
				};
		
		int max = insultes.length, min = 0 ;
		int aleat = min + (int)(Math.random() * ((max - min) + 1));
		
		TextComponent reponse = new TextComponent("§a[Répondre]");
		reponse.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT , new ComponentBuilder ("Répondre à ce boloss").create()));
		reponse.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/insulte " + p.getName() + ""));
		
		target.sendMessage(ChatColor.YELLOW + insultes[aleat] + ChatColor.RED+ "(" + p.getName() + ") ");
		target.spigot().sendMessage(reponse);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Seul les joueurs peuvent insulter <3");
			return true;
		} 
		final Player p = (Player)sender;
		if (commandLabel.equalsIgnoreCase("insulte")) {
			if (args.length != 1 || args.length == 0) {
				p.sendMessage(ChatColor.RED + "utilisation: /" + commandLabel + " <Pseudo>");
				return true;
			} 
			Player target = p.getServer().getPlayer(args[0]);
			if (target == null) {
				p.sendMessage(ChatColor.YELLOW + " Impossible de trouver le joueur " + ChatColor.GREEN + args[0]);
				return true;
			} 
			if (target.getName() == p.getName()) {
				p.sendMessage(ChatColor.YELLOW + "Juste, tu comptes vraiment t'insulter toi même ?");
				return true;
			}
   
			if (args.length == 1) {
				if (this.cooldownTime.containsKey(p)) {
					p.sendMessage(ChatColor.YELLOW + "Tu dois attendre " + ChatColor.GREEN + this.cooldownTime.get(p) + ChatColor.YELLOW + " secondes avant d'insulter quelqu'un.");
					return true;
				}
				p.sendMessage(ChatColor.YELLOW + "Tu as insulté " + ChatColor.GREEN + target.getName());
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

//TODO : Gestion de l'exception

/*[16:11:16 ERROR]: null
org.bukkit.command.CommandException: Unhandled exception executing command 'insulte' in plugin Eternia v1.0
at org.bukkit.command.PluginCommand.execute(PluginCommand.java:47) ~[patched_1.15.2.jar:git-Paper-91]
at org.bukkit.command.SimpleCommandMap.dispatch(SimpleCommandMap.java:159) ~[patched_1.15.2.jar:git-Paper-91]
at org.bukkit.craftbukkit.v1_15_R1.CraftServer.dispatchCommand(CraftServer.java:742) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.PlayerConnection.handleCommand(PlayerConnection.java:1820) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.PlayerConnection.a(PlayerConnection.java:1628) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.PacketPlayInChat.a(PacketPlayInChat.java:47) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.PacketPlayInChat.a(PacketPlayInChat.java:5) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.PlayerConnectionUtils.lambda$ensureMainThread$0(PlayerConnectionUtils.java:23) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.TickTask.run(SourceFile:18) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.IAsyncTaskHandler.executeTask(IAsyncTaskHandler.java:136) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.IAsyncTaskHandlerReentrant.executeTask(SourceFile:23) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.IAsyncTaskHandler.executeNext(IAsyncTaskHandler.java:109) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.MinecraftServer.ba(MinecraftServer.java:1038) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.MinecraftServer.executeNext(MinecraftServer.java:1031) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.IAsyncTaskHandler.awaitTasks(IAsyncTaskHandler.java:119) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.MinecraftServer.sleepForTick(MinecraftServer.java:1015) ~[patched_1.15.2.jar:git-Paper-91]
at net.minecraft.server.v1_15_R1.MinecraftServer.run(MinecraftServer.java:938) ~[patched_1.15.2.jar:git-Paper-91]
at java.lang.Thread.run(Unknown Source) [?:1.8.0_241]
Caused by: java.lang.ArrayIndexOutOfBoundsException: 4
at fr.ethan.eternia.Main.insulteAleat(Main.java:44) ~[?:?]
at fr.ethan.eternia.Main.onCommand(Main.java:75) ~[?:?]
at org.bukkit.command.PluginCommand.execute(PluginCommand.java:45) ~[patched_1.15.2.jar:git-Paper-91]
... 17 more*/