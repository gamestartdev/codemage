package org.gamestartschool.codemage.python;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.bukkit.potion.PotionEffectType;
import org.gamestartschool.codemage.ddp.ISpell;
import org.gamestartschool.codemage.ddp.ISpellMeteorMethodCaller;
import org.python.util.InteractiveInterpreter;

public class CodeRunner implements Runnable {
	private ISpellMeteorMethodCaller methodCaller;
	private static boolean exhaustQueueEachTick = true;
	public ConcurrentLinkedQueue<PythonMethodCall> pythonMethodQueue = new ConcurrentLinkedQueue<PythonMethodCall>();

	public CodeRunner(ISpellMeteorMethodCaller methodCaller) {
		this.methodCaller = methodCaller;
	}

	@Override
	public void run() {
		PythonMethodCall pythonMethodCall = pythonMethodQueue.poll();
		while (pythonMethodCall != null) {
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

	public void executeCode(final String code, final Player player, final Map<String, ISpell> libraries, final String spellname, final String spellId) {
		methodCaller.spellException("", spellId);
		String nonFinalCode = code;
		final List<ISpell> usedLibraries = new ArrayList<ISpell>();
		usedLibraries.add(libraries.get("xpRequirements"));
		usedLibraries.add(libraries.get("mpApi"));
		usedLibraries.add(libraries.get("preCode"));
		System.out.println(libraries.keySet().toString());
		for(String key : libraries.keySet())
		{
			if(code.contains("import " + key))
			{
				System.out.println(nonFinalCode);
				nonFinalCode = nonFinalCode.replaceAll("import " + key, "");
				usedLibraries.add(libraries.get(key));
				System.out.println(nonFinalCode);
			}
		}
		System.out.println(usedLibraries.toString());
		nonFinalCode = nonFinalCode.replaceAll("import", "iMpOrT");
		nonFinalCode = nonFinalCode.replaceAll("_importNms", "_importnms");
		nonFinalCode = nonFinalCode.replaceAll("_importCraft", "_importcraft");
		nonFinalCode = nonFinalCode.replaceAll("startTimestamp", "starttimestamp");
		nonFinalCode = nonFinalCode.replaceAll("jplayer", "Jplayer");
		nonFinalCode = nonFinalCode.replaceAll("\n", "\n	");
		nonFinalCode = nonFinalCode.replaceAll("studentCode", "studentcode");
		nonFinalCode = nonFinalCode.replaceAll("getattr", "gEtAtTr");
		nonFinalCode = nonFinalCode.replaceAll("setattr", "sEtAtTr");
		nonFinalCode = nonFinalCode.replaceAll("mc", "mC");
		nonFinalCode = nonFinalCode.replaceAll("mc_fast", "mC_fAsT");
		nonFinalCode = nonFinalCode.replaceAll("pythonMethodQueue", "pYtHoNmEtHoDqUeUe");
		nonFinalCode = nonFinalCode.replaceAll("exec", "eXeC");
		nonFinalCode = nonFinalCode.replaceAll("trace_function", "TrAcE_fUnCtIoN");
		nonFinalCode = "def studentCode():\n	" + nonFinalCode + "\nstudentCode()";
		final String sanitizedCode = nonFinalCode.replaceAll("__import__", "__iMpOrT__");
		@SuppressWarnings("unused")
		Future<InteractiveInterpreter> doNotBlockOnThisResultPlease = interpreterPool.submit(new Callable<InteractiveInterpreter>() {
			
			@Override
			public InteractiveInterpreter call() {
				InteractiveInterpreter pi = new InteractiveInterpreter();
				pi.set("jplayer", player);
				pi.set("pythonMethodQueue", pythonMethodQueue);
				pi.set("spellname", spellname);
				for (PotionEffectType p: PotionEffectType.values()) {
					if(p != null) //Someone thought it would be a good idea to put a null at the start.
					{
						pi.set(p.getName(), p);
					}
				}
				for (EntityType e : EntityType.values()) {
					if(e != EntityType.WITHER)
					{
						pi.set(e.toString(), e);
					}
					else
					{
						pi.set("WITHERBOSS", e);
					}
				}
				
				for (Material m : Material.values()) {
					pi.set(m.toString(), m);
				}
				
				for (Effect e : Effect.values()) {
					pi.set(e.toString(), e);
				}
				
				for (Sound s : Sound.values()) {
					pi.set(s.toString(), s);
				}
				
				String wrapperCode = "";
				for (ISpell spell : usedLibraries) {
					wrapperCode += spell.getCode() + "\n";
				}
				try {
					pi.exec(wrapperCode);
				} catch (Exception internale) {
					StringWriter isw = new StringWriter();
					internale.printStackTrace(new PrintWriter(isw));
					String itrace = isw.toString();
					internale.printStackTrace();
					if(spellId != "<console>") {
						methodCaller.spellException("Internal error:\r\n" + itrace, spellId);
					}
				}
				try {
					pi.exec(sanitizedCode);
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					String trace = sw.toString();
					e.printStackTrace();
					if(spellId != "<console>") {
						methodCaller.spellException(trace, spellId);
					}
				}
				
				pi.close();
				return pi;
			}
		});
	}
}
