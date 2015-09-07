package org.gamestartschool.codemage.python;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bukkit.command.CommandSender;
import org.gamestartschool.codemage.ddp.ISpell;
import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PySystemState;
import org.python.core.ThreadState;
import org.python.util.InteractiveInterpreter;
import org.python.util.PythonInterpreter;

public class CodeRunner {

	public void runCode(final String code, final CommandSender sender) {
		runCode(code, sender, new ArrayList<ISpell>());
	}

	public static void traceFunction() throws InterruptedException {
		System.out.println("Java sleeping...!");
		Thread.sleep(20);
	}

	public static String console(CommandSender sender, String cmd)
			throws InterruptedException, ExecutionException, IOException {
		System.out.println(sender);
		sender.getServer().dispatchCommand(sender, cmd);
		return "console complete";
	}

	private final ExecutorService interpreterPool = Executors.newFixedThreadPool(10);

	public void runCode(final String code, final CommandSender sender, List<ISpell> gameWrappers) {
		Future<String> result = executeCodeConcurrently(code, sender);
	}

	public Future<String> executeCodeConcurrently(final String code, final CommandSender sender) {
		return interpreterPool.submit(new Callable<String>() {
			@Override
			public String call() {
				PythonInterpreter pi = new PythonInterpreter();
				pi.set("player", sender);

				String preCode = "import sys\n";
				preCode += "from org.gamestartschool.codemage.python import CodeRunner\n";
				preCode += "def trace_function(frame, event, arg):\n";
				preCode += "	CodeRunner.traceFunction()\n";
				preCode += "	return trace_function\n";
				preCode += "def console(cmd):\n";
				preCode += "	CodeRunner.console(player, cmd)\n\n";
				preCode += "def teleport(x, y, z):\n";
				preCode += "	CodeRunner.console(player, 'tp %s %s %s' % (x,y,z))\n\n";
				preCode += "def setblock(x, y, z, type):\n";
				preCode += "	CodeRunner.console(player, 'setblock %s %s %s %s' % (x,y,z, type.lower()))\n\n";
				preCode += "sys.settrace(trace_function)\n";

				preCode += code;

				// for (ISpell spell : gameWrappers) {
				// preCode += spell.getCode() +"\n";
				// }

				// sender.sendMessage("About to execute code...: " + code);
				try {
					System.out.println("Running Code...");
					pi.exec(preCode);
					System.out.println("Done running...");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "success.";
			}
		});
	}

}
