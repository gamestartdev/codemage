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
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.core.ThreadState;
import org.python.util.InteractiveInterpreter;
import org.python.util.PythonInterpreter;

public class CodeRunnerThreadlessEpicFail {

	public ThreadState copyThreadState(ThreadState from, ThreadState to) {
		to.systemState = from.systemState;
		to.call_depth = from.call_depth;
		to.compareStateNesting = from.compareStateNesting;
		to.exception = from.exception;
		to.frame = from.frame;
		to.profilefunc = from.profilefunc;
		to.reprStack = from.reprStack;
		to.tracefunc = from.tracefunc;
		to.tracing = from.tracing;
		return to;
	}
	
	public void runCode(final String code, final CommandSender sender) {
		runCode(code, sender, new ArrayList<ISpell>());
	}
	
	public class CodeMageJavaAuthority {

		private InteractiveInterpreter pi;

		public CodeMageJavaAuthority(InteractiveInterpreter pi) {
			this.pi = pi;
		}
		public void StartStop(){
			System.out.println("Authority Stopping!");
			
			PyObject savedLocals =  pi.getLocals().invoke("copy");
			PySystemState savedSystemState = pi.getSystemState();
			ThreadState savedThreadState = new ThreadState(savedSystemState);
			copyThreadState(Py.getThreadState(savedSystemState), savedThreadState);
			
			pi.getSystemState().callExitFunc();
			
			System.out.println("About to resume...");
			pi = new InteractiveInterpreter(savedLocals, savedSystemState);
			copyThreadState(Py.getThreadState(savedSystemState), savedThreadState);

			pi.exec(preCode);
			System.out.println("Done resuming!!!!!!");
		}
		
		public void traceFunction() throws InterruptedException {
			System.out.println("Java sleeping...!");
			Thread.sleep(20);
		}
		
		public String console(CommandSender sender, String cmd)
				throws InterruptedException, ExecutionException, IOException {
			System.out.println(sender);
			sender.getServer().dispatchCommand(sender, cmd);
			return "console complete";
		}
	}

	private final ExecutorService interpreterPool = Executors.newFixedThreadPool(10);

	public void runCode(final String code, final CommandSender sender, List<ISpell> gameWrappers) {
//		Future<String> result = executeCodeConcurrently(code, sender);
//		System.out.println(result.get());
		executeCode(sender);
	}


	private static String preCode;
	private String executeCode(final CommandSender sender) {
		InteractiveInterpreter pi = new InteractiveInterpreter();
		CodeMageJavaAuthority authority = new CodeMageJavaAuthority(pi);
		
		pi.set("player", sender);
		pi.set("authority", authority);
		pi.set("x", 0);
		
		preCode = "import sys\n";
		preCode += "def trace_function(frame, event, arg):\n";
		preCode += "	authority.traceFunction()\n";
		preCode += "	return trace_function\n";
		preCode += "def console(cmd):\n";
		preCode += "	authority.console(player, cmd)\n\n";
		preCode += "def teleport(x, y, z):\n";
		preCode += "	authority.console(player, 'tp %s %s %s' % (x,y,z))\n\n";
		preCode += "def setblock(x, y, z, type):\n";
		preCode += "	authority.console(player, 'setblock %s %s %s %s' % (x,y,z, type.lower()))\n\n";
//		preCode += "sys.settrace(trace_function)\n";
		preCode += "x=x+5\n";
		preCode += "print x\n";
		preCode += "print 'Python about to call Authority...'\n";
		preCode += "authority.StartStop()\n";
		preCode += "print 'resuming!!!!!!!!'\n";
//		preCode += code;

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
	
	public Future<String> executeCodeConcurrently(final String code, final CommandSender sender) {
		return interpreterPool.submit(new Callable<String>() {
			@Override
			public String call() {
				return executeCode(sender);
			}
		});
	}

}
