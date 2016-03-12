package org.gamestartschool.codemage.python;


import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.gamestartschool.codemage.ddp.ISpell;

public class PythonConsoleCommand implements CommandExecutor {

	private CodeRunner codeRunner;

	public PythonConsoleCommand(CodeRunner codeRunner) {
		this.codeRunner = codeRunner;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("python") && args.length == 1) {
			String code = args[0].toString();
			codeRunner.executeCode(code, null, new ArrayList<ISpell>(), "<console>", "<console>");
			return true;
		} else {
			sender.sendMessage("Sry, the `python` command only understands a single line without spaces right now...");
		}
		return false;
	}
}
