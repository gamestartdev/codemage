package org.gamestartschool.codemage.python;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.gamestartschool.codemage.ddp.CodeMageDDP;
import org.gamestartschool.codemage.ddp.ISpell;
import org.gamestartschool.codemage.ddp.IUser;
import org.python.core.PyObject;
import org.python.core.PyStringMap;
import org.python.core.PySystemState;
import org.python.util.InteractiveInterpreter;

public class CodeMagePythonSpigotPlugin extends JavaPlugin {

	String meteorIp = "localhost";
	int meteorPort = 3000;
	String meterUsername = "admin2";
	String meteorPassword = "asdf";
	String minecraftPlayerId1 = "GameStartSchool";
	String minecraftPlayerId2 = "denrei";
	CodeMageDDP ddp = null;
	CodeRunner codeRunner;
	PySystemState state;
	PyStringMap locals;
	private int codeRunnerTaskId = -1;
	

	void log(String message) {
		getServer().getLogger().info(message);
	}

	public void onEnable() {
		try {
			log("DDP Plugin being invoked from CodeMagePython.");
			ddp = new CodeMageDDP(meteorIp, meteorPort);
			log("DDP Plugin attempting to connect....");
			ddp.connect(meterUsername, meteorPassword);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		initCodeRunner();
		PrintHelper.setMethodCaller(ddp.getMethodCaller());
		this.getCommand("python").setExecutor(new PythonConsoleCommand(codeRunner, ddp.getRunBeforeStudentCode()));
		this.getCommand("reconnectddp").setExecutor(new DDPReconnectCommand(ddp));
		this.getCommand("reloadwrappers").setExecutor(new GameWrapperReloadCommand(this));
		addListeners();
	}
	
	public void initCodeRunner()
	{
		final ISpell[] gameWrappers = ddp.getAllGameWrappers();
		ExecutorService initInterpreterPool = Executors.newFixedThreadPool(1);
		InteractiveInterpreter initInterpreter = null;
		try {
			initInterpreter = initInterpreterPool.submit(new Callable<InteractiveInterpreter>() {
				@Override
				public InteractiveInterpreter call()
				{
					InteractiveInterpreter pi = new InteractiveInterpreter();
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
					for (ISpell spell : gameWrappers) {
						wrapperCode += spell.getCode() + "\n";
					}
					System.out.println(wrapperCode);
					wrapperCode = wrapperCode.replaceAll("jplayer", "injectedstorage['jplayer']"); //Python is a derp.
					wrapperCode = wrapperCode.replaceAll("pythonMethodQueue", "injectedstorage['pythonMethodQueue']");
					wrapperCode = wrapperCode.replaceAll("spellId", "injectedstorage['spellId']");
					wrapperCode = wrapperCode.replaceAll("spellname", "injectedstorage['spellname']");
					
					try {
						pi.exec(wrapperCode);
					} catch (Exception wrappere) {
						StringWriter wsw = new StringWriter();
						wrappere.printStackTrace(new PrintWriter(wsw));
						String wtrace = wsw.toString();
						wrappere.printStackTrace();
					}
					return pi;
				}
			}).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		state = initInterpreter.getSystemState();
		PyStringMap localsMap = (PyStringMap)initInterpreter.getLocals();
		locals = localsMap.copy();
		initInterpreter.close();
		initInterpreterPool.shutdown();
		if(codeRunnerTaskId != -1) {
			getServer().getScheduler().cancelTask(codeRunnerTaskId);
		}
		codeRunner = new CodeRunner(ddp.getMethodCaller(), state, locals);
		codeRunnerTaskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, codeRunner, 0L, 1L);
	}

	private void addListeners() {
		PluginManager pluginManager = getServer().getPluginManager();
		// pluginManager.registerEvents(new CodeMageListener(), this);

		EventExecutor executor = new EventExecutor() {
			public void execute(Listener listener, Event ev) throws EventException {
				PlayerInteractEvent event = (PlayerInteractEvent) ev;
				if (event.hasItem()) {
					Player player = event.getPlayer();
					IUser user = ddp.getUser(player.getName());
					Material material = event.getItem().getType();
					Action action = event.getAction();
					System.out.println("Item Type: " + material);
					System.out.println("Binding Type: " + action);
					
					Map<String, ISpell> libraries = ddp.getAllLibraries();
					List<ISpell> spells = user.getSpells(material, action);
					for (ISpell spell : spells) {
						log("runningCode: " + spell.getCode());
						codeRunner.executeCode(spell.getCode(), player, libraries, spell.getName(), spell.getId(), ddp.getRunBeforeStudentCode());
					}
				}
			}
		};

		final Class<? extends Event> eventClass = PlayerInteractEvent.class;
		final Listener listener = new Listener() {
		};
		EventPriority priority = EventPriority.NORMAL;
		pluginManager.registerEvent(eventClass, listener, priority, executor, this);
	}

	public void onDisable() {
		ddp.disconnect();
	}

}
