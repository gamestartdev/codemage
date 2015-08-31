package org.gamestartschool.codemage.python;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PySyntaxError;
import org.python.util.PythonInterpreter;

public class PythonCommand implements CommandExecutor {

	private final CodeMagePythonSpigotPlugin plugin;

	public PythonCommand(CodeMagePythonSpigotPlugin plugin) {
		this.plugin = plugin; // Store the plugin in situations where you need
								// it.
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("python") && args.length == 1) {
			String code = args[0].toString();
			plugin.runCode(sender, code);
			return true;
		} else {
			sender.sendMessage("Sry, the `python` command only understands a single line without spaces right now...");
		}
		return false;
	}
}
