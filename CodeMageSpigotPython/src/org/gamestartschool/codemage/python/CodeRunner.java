package org.gamestartschool.codemage.python;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
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
		String nonFinalCode = code.replaceAll("import ", "fjhgjkdhldjfnkgfxlnvlizjfdrnboezaiudlouysgrhn ");
		nonFinalCode = nonFinalCode.replaceAll("_importNms", "vhsjkdfvgkfxjgvhjfx");
		nonFinalCode = nonFinalCode.replaceAll("_importCraft", "vkjncjfnjzdnbhxjkvnkjhxbvj");
		final String importsRemoved = code.replaceAll("__import__", "raise NameError('You do not have permission to do that!')#");
		Future<InteractiveInterpreter> doNotBlockOnThisResultPlease = interpreterPool.submit(new Callable<InteractiveInterpreter>() {
			
			@Override
			public InteractiveInterpreter call() {
				InteractiveInterpreter pi = new InteractiveInterpreter();
				pi.set("player", player);
				pi.set("pythonMethodQueue", pythonMethodQueue);

				for (EntityType e : EntityType.values()) {
					pi.set(e.toString(), e);
				}
				
				for (Material m : Material.values()) {
					pi.set(m.toString(), m);
				}
				
				for (Effect e : Effect.values()) {
					pi.set(e.toString(), e);
				}
				
				for (Sound s : Sound.values()) {
					if(s != Sound.GLASS && s != Sound.WATER && s != Sound.LAVA && s != Sound.PORTAL)
					{
						pi.set(s.toString(), s);
					}
					else
					{
						pi.set(s.toString() + "_SOUND", s);
					}
				}
				
				String wrapperCode = "";
				for (ISpell spell : gameWrappers) {
					wrapperCode += spell.getCode() + "\n";
				}

				try {
					pi.exec(wrapperCode + importsRemoved);
				} catch (Exception e) {
					e.printStackTrace();
				}
				pi.close();
				return pi;
			}
		});
	}
}
