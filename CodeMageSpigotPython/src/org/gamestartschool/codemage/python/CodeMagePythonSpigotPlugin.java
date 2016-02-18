package org.gamestartschool.codemage.python;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.bukkit.Material;
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
import org.gamestartschool.codemage.ddp.CodeMageDDP;
import org.gamestartschool.codemage.ddp.IEnchantment;
import org.gamestartschool.codemage.ddp.ISpell;
import org.gamestartschool.codemage.ddp.IUser;

public class CodeMagePythonSpigotPlugin extends JavaPlugin {

	String meteorIp = "localhost";
	int meteorPort = 3000;
	String meterUsername = "admin2";
	String meteorPassword = "asdf";
	String minecraftPlayerId1 = "GameStartSchool";
	String minecraftPlayerId2 = "denrei";
	CodeMageDDP ddp = null;
	CodeRunner codeRunner;

	void log(String message) {
		getServer().getLogger().info(message);
	}

	public void onEnable() {
		codeRunner = new CodeRunner();
		this.getCommand("python").setExecutor(new PythonConsoleCommand(codeRunner));
		addListeners();
		getServer().getScheduler().scheduleSyncRepeatingTask(this, codeRunner, 0L, 1L);

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

					List<ISpell> gameWrappers = ddp.getAllGameWrappers();
					System.out.println("gameWrappers: " + gameWrappers.size());
					List<IEnchantment> enchantments = user.getEnchantments(material, action);
					for (IEnchantment e : enchantments) {
						List<ISpell> spells = e.getSpells();
						for (ISpell spell : spells) {
							log("runningCode: " + spell.getCode());
							codeRunner.executeCode(spell.getCode(), player, gameWrappers, spell.getName());
						}
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

	private boolean checkJythonAccess() {
		if (new File("plugins/lib/jython.jar").exists()) {
			return true;
		}
		log("Could not find {serverDir}/plugins/lib/jython.jar");
		return false;
	}

}
