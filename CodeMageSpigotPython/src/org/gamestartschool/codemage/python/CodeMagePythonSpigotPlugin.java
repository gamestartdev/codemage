package org.gamestartschool.codemage.python;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Queue;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
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
import org.python.modules.synchronize;
import org.python.util.InteractiveInterpreter;

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

				List<IEnchantment> enchantments = user.getEnchantments(material, action);
				for (IEnchantment e : enchantments) {
					List<ISpell> spells = e.getSpells();
					for (ISpell spell : spells) {
						log("runningCode: " + spell.getCode());
						runCode(player, spell.getCode());
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

	private void log(String message) {
		getServer().getLogger().info(message);
	}

	public void onEnable() {
		this.getCommand("python").setExecutor(new PythonCommand(this));

		// BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		// scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
		// @Override
		// public void run() {
		// System.out.println("Task!");
		// }
		// }, 0L, 20L);

		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new TriggerListener(), this);

		try {
			log("DDP Plugin being invoked from CodeMagePython.");
			ddp = new CodeMageDDP(meteorIp, meteorPort);
			log("DDP Plugin attempting to connect....");
			ddp.connect(meterUsername, meteorPassword);

			runCode(null, "print 'all done'\n");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void onDisable() {
		ddp.disconnect();
	}

	public void runCode(final CommandSender sender, final String code) {
		// PythonInterpreter pi =
		// PythonInterpreter.threadLocalStateInterpreter(new
		// PyDictionary());
		final InteractiveInterpreter pi = new InteractiveInterpreter();

		String preCode = "";
		preCode += "import pdb\n";
		preCode += "from org.bukkit.event import EventPriority\n";
		preCode += "from org.bukkit import Location, Material\n";
		preCode += "from org.bukkit.block import Block\n";
		preCode += "from org.bukkit import Material\n";

		// preCode += "from org.gamestartschool.codemage.python
		// import
		// CodeMagePythonSpigotPlugin\n";
		// preCode += "import __builtin__\n";
		// preCode += "__builtin__.__import__, __builtin__.reload =
		// None, None\n";

		// preCode += "print 'about to set trace'\n";
		// preCode += "pdb.set_trace()\n";

		// preCode += "friends = ['carlos', 'matt', 'nate']\n";
		// preCode += "for x in friends:\n";
		// preCode += " print x\n";
		//
		// preCode += "import time\n";
		// preCode += "for x in range(20):\n";
		// preCode += " time.sleep(.5)\n";
		// preCode += " print 'PY: ' + str(x)\n";
		// preCode += "print 'dont somethign ELSE'\n";

		if (sender instanceof Player) {
			Player player = (Player) sender;
			sender.sendMessage("Found you as Player: " + player.getName());
			pi.set("player", player);
			preCode += "from org.bukkit.entity import Player as _Player\n";
			preCode += "class PlayerWrap():\n";
			preCode += "	def __init__(self, p):\n";// *args,**kwargs
			preCode += "		self.p = p\n";
			preCode += "		print 'doing it'\n";
			preCode += "	def blink(self):\n";
			preCode += "		loc = self.p.getLocation()\n";
			preCode += "		print loc\n";
			preCode += "		loc.setY(100)\n";
			preCode += "		print loc\n";
			preCode += "		self.p.teleport(loc)\n";
			preCode += "		print 'teleported!'\n";
			preCode += "p = PlayerWrap(player)\n";
		}

		// sender.sendMessage("About to execute code...: " + code);
		pi.set("result", 5);
		pi.exec(preCode + code);
		// PyObject pyResult = pi.get("result");
		// sender.sendMessage("Finished executing code!");
		// System.out.println("LOCALS:");
		// System.out.println(pi.getSystemState());
		// System.out.println(pi.getLocals());
		pi.close();
	}
}
