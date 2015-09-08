package org.gamestartschool.codemage.python;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.gamestartschool.codemage.ddp.CodeMageDDP;
import org.gamestartschool.codemage.ddp.IEnchantment;
import org.gamestartschool.codemage.ddp.ISpell;
import org.gamestartschool.codemage.ddp.IUser;

public class CodeMagePythonSpigotPlugin extends JavaPlugin {

	public class TriggerListener implements Listener {

		@EventHandler
		public void onPlayerMove(PlayerMoveEvent event) {
			// Location loc = event.getPlayer().getLocation();
			// loc.setY(loc.getY() + 5);
			// Block b = loc.getBlock();
			// b.setType(Material.STONE);
			Player player = event.getPlayer();
			// player.getUniqueId();
			// runCode(player, codeProvider.getCode());
		}

		@EventHandler
		public void onPlayerInteract(PlayerInteractEvent event) {
			if (event.hasItem()) {
				Player player = event.getPlayer();
				IUser user = ddp.getUser(player.getName());
				Material material = event.getItem().getType();
				Action action = event.getAction();
				System.out.println("Item Type: " + material);
				System.out.println("Binding Type: " + action);
				
				List<ISpell> gameWrappers = user.getGameWrappers();
				System.out.println("gameWrappers: " +gameWrappers.size());
				List<IEnchantment> enchantments = user.getEnchantments(material, action);
				for (IEnchantment e : enchantments) {
					List<ISpell> spells = e.getSpells();
					for (ISpell spell : spells) {
						log("runningCode: " + spell.getCode());
						codeRunner.runCode(spell.getCode(), player, gameWrappers);
					}
				}
			}
		}
	}

	String meteorIp = "localhost";
	int meteorPort = 3000;
	String meterUsername = "admin2";
	String meteorPassword = "asdf";
	String minecraftPlayerId1 = "GameStartSchool";
	String minecraftPlayerId2 = "denrei";
	CodeMageDDP ddp = null;
	private CodeRunner codeRunner;

	private void log(String message) {
		getServer().getLogger().info(message);
	}
	public ConcurrentLinkedQueue<PythonMethodCall> pythonMethodQueue = new ConcurrentLinkedQueue<PythonMethodCall>();

	public void onEnable() {
		
		codeRunner = new CodeRunner(pythonMethodQueue);
		this.getCommand("python").setExecutor(new PythonCommand(this, codeRunner));
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new TriggerListener(), this);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, pythonCommandExecutionQueue, 0L, 1L);
		
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

	public void onDisable() {
		ddp.disconnect();
	}
	
	public final class PythonCommandExecutionQueue implements Runnable {

		@Override
		public void run() {
			PythonMethodCall pythonMethodCall = pythonMethodQueue.poll();
			if(pythonMethodCall != null) {
				try{
					pythonMethodCall.result = pythonMethodCall.method.__call__(pythonMethodCall.args);
				} catch (Exception e) {
					e.printStackTrace();
					pythonMethodCall.message = e.toString();
				} finally{
					pythonMethodCall.isDone = true;
				}
			}
		}
	}
}
