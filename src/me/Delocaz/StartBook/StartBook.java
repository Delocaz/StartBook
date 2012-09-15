package me.Delocaz.StartBook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;

public class StartBook extends JavaPlugin implements Listener {
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getConfig().addDefaults(YamlConfiguration.loadConfiguration(getClass().getResourceAsStream("/config.yml")));
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label == "reload") {
			sender.sendMessage("StartBook does NOT approve of reloads.");
			return true;
		}
		if (sender instanceof Player) {
			if (args.length == 0) {
				((Player) sender).getInventory().addItem(getBook());
			} else {
				if (args.length == 1) {
					if (Bukkit.matchPlayer(args[0]).isEmpty()) {
						sender.sendMessage(colorize("&cPlayer not found!"));
					} else {
						Player p = Bukkit.matchPlayer(args[0]).get(0);
						sender.sendMessage(colorize("&aStart book sent to "+p.getDisplayName()+"!"));
						p.getInventory().addItem(getBook());
					}
				}
			}
		} else {
			if (args.length == 0) {
				sender.sendMessage(colorize("&cYou have to be a player to do this! Use /startbook <player> if you want to give a player a book from the console."));
			} else {
				if (args.length == 1) {
					if (Bukkit.matchPlayer(args[0]).isEmpty()) {
						sender.sendMessage(colorize("&cPlayer not found!"));
					} else {
						Player p = Bukkit.matchPlayer(args[0]).get(0);
						sender.sendMessage(colorize("&aStart book sent to "+p.getDisplayName()+"!"));
						p.getInventory().addItem(getBook());
					}
				}
			}
		}
		return true;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		reloadConfig();
		Player p = e.getPlayer();
		if (!p.hasPlayedBefore()) {
			p.getInventory().addItem(getBook());
		}
	}
	public ItemStack getBook() {
		BookItem bi = new BookItem(new ItemStack(387,1));
		bi.setTitle(getConfig().getString("title"));
		bi.setAuthor(getConfig().getString("author"));
		String pgs = colorize(getConfig().getString("text"));
		pgs = pgs.replace("\n\n", "\n"+ChatColor.BLACK+"\n");
		bi.setPages(split(pgs, 255));
		return bi.getItemStack();
	}
	public String colorize(String s) {
		s = ChatColor.translateAlternateColorCodes('&', s);
		s = s.replaceAll("&k", ChatColor.MAGIC.toString());
		s = s.replaceAll("&l", ChatColor.BOLD.toString());
		s = s.replaceAll("&o", ChatColor.ITALIC.toString());
		s = s.replaceAll("&n", ChatColor.UNDERLINE.toString());
		s = s.replaceAll("&m", ChatColor.STRIKETHROUGH.toString());
		s = s.replaceAll("&r", ChatColor.RESET.toString());
		return s;
	}
	public String[] split(String source, int length) {
		return Iterators.toArray(Splitter.fixedLength(length).split(source).iterator(), String.class);
	}
}
