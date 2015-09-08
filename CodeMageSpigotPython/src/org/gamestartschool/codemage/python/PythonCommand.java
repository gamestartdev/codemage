package org.gamestartschool.codemage.python;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PythonCommand implements CommandExecutor {

	private final CodeMagePythonSpigotPlugin plugin;
	private CodeRunner codeRunner;

	public PythonCommand(CodeMagePythonSpigotPlugin plugin, CodeRunner codeRunner) {
		this.plugin = plugin;
		this.codeRunner = codeRunner;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("python") && args.length == 1) {
			String code = args[0].toString();
			codeRunner.runCode(code, sender);
			return true;
		} else {
			sender.sendMessage("Sry, the `python` command only understands a single line without spaces right now...");
		}
		return false;
	}
}
