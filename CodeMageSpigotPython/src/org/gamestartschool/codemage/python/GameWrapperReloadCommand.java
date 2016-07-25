package org.gamestartschool.codemage.python;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameWrapperReloadCommand implements CommandExecutor {
	
	private CodeMagePythonSpigotPlugin plugin;
	
	public GameWrapperReloadCommand(CodeMagePythonSpigotPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("reloadwrappers") && sender.isOp())
		{
			sender.sendMessage("Reloading gamewrappers...");
			plugin.initCodeRunners();
			//Un-schedule the old CodeRunner and schedule the new one
			//plugin.getServer().getScheduler().cancelTask(taskId);
			//plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, plugin.codeRunner, 0L, 1L);
			sender.sendMessage("Reloaded gamewrappers.");
			return true;
		}
		return false;
	}

}
