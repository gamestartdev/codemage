package org.gamestartschool.codemage.python;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gamestartschool.codemage.ddp.ISpell;
import org.python.core.PyObject;
import org.python.util.InteractiveInterpreter;

public class CodeRunner {

	private ConcurrentLinkedQueue<PythonMethodCall> pythonMethodQueue;

	public CodeRunner(ConcurrentLinkedQueue<PythonMethodCall> pythonMethodQueue) {
		this.pythonMethodQueue = pythonMethodQueue;
	}

	public void runCode(final String code, final CommandSender sender) {
		runCode(code, sender, new ArrayList<ISpell>());
	}
	
	public class CodeMageJavaAuthority {
		
		public void traceFunction() throws InterruptedException {
			System.out.println("Java tracing...!");
//			Thread.sleep(20);
		}
		
		public PyObject call(PyObject method, PyObject[] args) throws InterruptedException {
			System.out.println("CodeMageJavaAuthority call");
			PythonMethodCall pythonMethodCall = new PythonMethodCall(method, args);
			pythonMethodQueue.offer(pythonMethodCall);
			System.out.println("CodeMageJavaAuthority offered");
			while(!pythonMethodCall.isDone()) {
				System.out.println("!pythonMethodCall.isDone()");
				Thread.sleep(1);
			}
			return pythonMethodCall.get();
		}
	}

	private final ExecutorService interpreterPool = Executors.newFixedThreadPool(10);

	public void runCode(final String code, final CommandSender sender, List<ISpell> gameWrappers) {
		Future<String> result = executeCodeConcurrently(code, sender);
	}
	
	public Future<String> executeCodeConcurrently(final String code, final CommandSender sender) {
		return interpreterPool.submit(new Callable<String>() {
			@Override
			public String call() {
				return executeCode(code, sender, new CodeMageJavaAuthority());
			}
		});
	}

	private static String preCode;
	private String executeCode(String code, final CommandSender sender, CodeMageJavaAuthority authority) {
		InteractiveInterpreter pi = new InteractiveInterpreter();
		pi.set("player", (Player)sender);
		pi.set("authority", authority);
		pi.set("x", 0);
		
		preCode = "import sys\n";
		preCode += "from org.bukkit import Location, Material\n";
		preCode += "from org.bukkit.Material import *\n";
		preCode += "def trace_function(frame, event, arg):\n";
		preCode += "	authority.traceFunction()\n";
		preCode += "	return trace_function\n";
		preCode += "def call(method, *args):\n";
		preCode += "	return authority.call(method, args)\n";
		
		preCode += "def loc(x,y,z):\n";
		preCode += "	loc = player.getLocation()\n";
		preCode += "	loc.setX(x)\n";		
		preCode += "	loc.setY(y)\n";		
		preCode += "	loc.setZ(z)\n";				
		preCode += "	return loc\n\n";
		
		preCode += "def setblock(x,y,z, type):\n";
		preCode += "	return call(loc(x,y,z).getBlock().setType, type)\n\n";
		
		preCode += "def teleport(x,y,z):\n";
		preCode += "	return call(player.teleport, loc(x,y,z))\n\n";
		preCode += "def getPlayerLoc():\n";
		preCode += "	return call(player.getLocation)\n\n";
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
		pi.close();
		return "success.";
	}
	


}
