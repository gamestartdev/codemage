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
			plugin.initCodeRunner();
			return true;
		}
		return false;
	}

}
