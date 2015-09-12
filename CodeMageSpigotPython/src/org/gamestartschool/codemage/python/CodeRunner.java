package org.gamestartschool.codemage.python;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bukkit.entity.Player;
import org.gamestartschool.codemage.ddp.ISpell;
import org.python.util.InteractiveInterpreter;

public class CodeRunner implements Runnable {
	private static boolean exhaustQueueEachTick = true;
	public ConcurrentLinkedQueue<PythonMethodCall> pythonMethodQueue = new ConcurrentLinkedQueue<PythonMethodCall>();

	@Override
	public void run() {
		PythonMethodCall pythonMethodCall = pythonMethodQueue.poll();
		int i = 0;
		while (pythonMethodCall != null) {
			i++;
			try {
				pythonMethodCall.result = pythonMethodCall.method.__call__(pythonMethodCall.args);
			} catch (Exception e) {
				e.printStackTrace();
				pythonMethodCall.message = e.toString();
			} finally {
				pythonMethodCall.isDone = true;
			}
			if (exhaustQueueEachTick){
				pythonMethodCall = pythonMethodQueue.poll();
			}
		}
//		System.out.println("Ran " + i + " PythonMethodCalls.");
	}

	private final ExecutorService interpreterPool = Executors.newFixedThreadPool(25);

	public void executeCode(final String code, final Player player, final List<ISpell> gameWrappers) {
		
		Future<InteractiveInterpreter> doNotBlockOnThisResultPlease = interpreterPool.submit(new Callable<InteractiveInterpreter>() {
			
			@Override
			public InteractiveInterpreter call() {
				InteractiveInterpreter pi = new InteractiveInterpreter();
				pi.set("player", player);
				pi.set("pythonMethodQueue", pythonMethodQueue);

				String wrapperCode = "";
				for (ISpell spell : gameWrappers) {
					wrapperCode += spell.getCode() + "\n";
				}

				try {
					pi.exec(wrapperCode + code);
				} catch (Exception e) {
					e.printStackTrace();
				}
				pi.close();
				return pi;
			}
		});
	}
}
