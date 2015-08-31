package org.gamestartschool.codemage.python;

import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.gamestartschool.codemage.ddp.CodeMageDDP;
import org.gamestartschool.codemage.ddp.EDummyEnchantmentBinding;
import org.gamestartschool.codemage.ddp.EDummyEnchantmentTrigger;
import org.gamestartschool.codemage.ddp.IEnchantment;
import org.gamestartschool.codemage.ddp.ISpell;
import org.gamestartschool.codemage.ddp.IUser;
import org.hamcrest.core.IsInstanceOf;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PySyntaxError;
import org.python.util.PythonInterpreter;

public class CodeMagePythonSpigotPlugin extends JavaPlugin {

	public class PlayerInteractListener implements Listener {

		@EventHandler
		public void onPlayerInteract(PlayerInteractEvent event) {
			
			// Location loc = event.getPlayer().getLocation();
			// loc.setY(loc.getY() + 5);
			// Block b = loc.getBlock();
			// b.setType(Material.STONE);
			Player player = event.getPlayer();
			event.getItem();
			event.hasItem();

			log("onPlayerInteract "+ player.getName());

			IUser user = ddp.getUser(player.getName());
			List<IEnchantment> enchantments = user.getEnchantments();
			
			log("enchantments "+ enchantments.size());
			for (IEnchantment e : enchantments) {
				if (EDummyEnchantmentTrigger.PRIMARY.equals(e.getTrigger())
						&& EDummyEnchantmentBinding.WOODEN_SWORD.equals(e.getBinding())) {
					List<ISpell> spells = e.getSpells();
					log("spells: "+ spells.size());
					for (ISpell spell : spells) {
						// spell.setStatus(true);
						log("runningCode: "+ spell.getCode());
						runCode(player, spell.getCode());
					}
				}
			}
		}
	}

	public class PlayerMoveListener implements Listener {

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

		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new PlayerMoveListener(), this);
		pluginManager.registerEvents(new PlayerInteractListener(), this);

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

	public void runCode(CommandSender sender, String code) {
		try {
			PythonInterpreter pi = new PythonInterpreter();
			if(sender instanceof Player){
				Player player = (Player)sender;
				sender.sendMessage("Found you as Player: " + player.getName());
				pi.set("player", player);
				
				String preCode = "";
//				preCode += "import pdb\n";
				preCode += "from org.bukkit.event import EventPriority\n";
//				preCode += "from org.gamestartschool.codemage.python import CodeMagePythonSpigotPlugin\n";
				preCode += "from org.bukkit.entity import Player as _Player\n";
//				preCode += "import __builtin__\n";
//				preCode += "__builtin__.__import__, __builtin__.reload = None, None\n";
				preCode += "class PlayerWrap():\n";
				preCode += "	def __init__(self, p):\n"; //, *args, **kwargs
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
				
//				preCode += "loc = player.getLocation()\n";
//				preCode += "loc.setY(200)\n";
//				preCode += "player.teleport(loc)\n";
//				preCode += "print 'setting trace...'\n";
//				preCode += "pdb.set_trace()\n";
//				preCode += "for x in range(['carlos', 'matt', 'nate']):\n";
//				preCode += "	print x\n";
//				preCode += "p.blink()\n";
				preCode += "print 'Done Blinking'\n";
				code = preCode + code;
			}
			
			sender.sendMessage("About to execute code...: " + code);
			pi.set("result", 5);
			pi.exec(code);
			PyObject pyResult = pi.get("result");
			sender.sendMessage("Finished executing code!");
			pi.close();
		} catch (PySyntaxError e) {
			e.printStackTrace();
		} catch (PyException e) {
			e.printStackTrace();
		}
	}
}
