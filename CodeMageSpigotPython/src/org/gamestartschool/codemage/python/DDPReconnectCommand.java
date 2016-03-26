package org.gamestartschool.codemage.python;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gamestartschool.codemage.ddp.CodeMageDDP;

public class DDPReconnectCommand implements CommandExecutor {
	
	private CodeMageDDP ddp;
	
	public DDPReconnectCommand(CodeMageDDP ddp)
	{
		this.ddp = ddp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("reconnectddp") && sender.isOp())
		{
			ddp.disconnect();
			try {
				ddp.connect("admin2", "asdf");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
