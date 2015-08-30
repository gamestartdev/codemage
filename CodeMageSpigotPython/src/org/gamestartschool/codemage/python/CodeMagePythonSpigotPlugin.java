package org.gamestartschool.codemage.python;

import java.net.URISyntaxException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.gamestartschool.codemage.ddp.CodeMageDDP;
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PySyntaxError;
import org.python.util.PythonInterpreter;

import jnr.ffi.Struct.pid_t;

public class CodeMagePythonSpigotPlugin extends JavaPlugin {

	private class PlayerMoveListener implements Listener {
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent event) {
			Location loc = event.getPlayer().getLocation();
			loc.setY(loc.getY() + 5);
			Block b = loc.getBlock();
			b.setType(Material.STONE);
		}
	}

	protected PluginManager pm;

	public void onDisable() {
	}

	public void onEnable() {
		getServer().getLogger().severe("CodeMagePython Enabled");
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
	}

	@Override
	public void onLoad() {
		getServer().getLogger().severe("CodeMagePython Loaded");
	}

	private void log(String msg) {
		getServer().getLogger().severe(msg);
	}

	public static String WORDS = "Omfg";
	public static void blink(Player player) {
		Location loc = player.getLocation();
		loc.setY(loc.getY() + 50);
		player.teleport(loc);
	}
	
	private String getCode() {
		String code = "import sys\n";
		code += "from org.bukkit.event import EventPriority\n";
		code += "from org.gamestartschool.codemage.python import CodeMagePythonSpigotPlugin\n";
		code += "from org.bukkit.entity import Player as _Player\n";
		code += "class PlayerWrap():\n";
		code += "	def __init__(self, p):\n"; //, *args, **kwargs
		code += "		self.p = p\n";
		code += "		print 'doing it'\n";
		code += "	def blink(self):\n";
		code += "		loc = self.p.getLocation()\n";
		code += "		print loc\n";
		code += "		loc.setY(100)\n";
		code += "		print loc\n";
		code += "		self.p.teleport(loc)\n";
		code += "		print 'teleported!'\n";
		code += "p = PlayerWrap(player)\n";
		
//		code += "loc = player.getLocation()\n";
//		code += "loc.setY(200)\n";
//		code += "player.teleport(loc)\n";
		code += "p.blink()\n";
		code += "print 'Done Blinking'\n";
		
		// code += "import org.bukkit as bukkit\n";
		// code += "from java.util.logging import Level\n";
		// code += "import org.cyberlis.pyloader.PythonPlugin as
		// PythonPlugin\n";
		// code += "import org.cyberlis.pyloader.PythonListener as
		// _PythonListener\n";
		code += "result=789\n";
		return code;
	}

	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("blink")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				executePython(player, getCode());
				// Sets loc to five above where it used to be. Note that this
				// doesn't change the player's position.
			} else {
				sender.sendMessage("You must be a player!");
				return false;
			}
		}

		if (cmd.getName().equalsIgnoreCase("python")) {
//			executePython(sender, getCode());

			// String code = args[0].toString();
			// String ddpResult = ddp(sender);
			// log("Operation: " + ddpResult);
		}
		return false;
	}

	private void executePython(Player sender, String code) {
		if (sender instanceof Player) {
			try {
				PythonInterpreter pi = new PythonInterpreter();
				Player player = (Player) sender;
				pi.set("player", player);
				pi.exec(code);
				PyObject pyResult = pi.get("result");
				String resultMessage = pyResult.toString();
				log(resultMessage);
				sender.sendMessage("Result of code: " + resultMessage);
				pi.close();
			} catch (PySyntaxError e) {
				e.printStackTrace();
			} catch (PyException e) {
				e.printStackTrace();
			}
//			Location loc = player.getLocation();
			// Sets loc to five above where it used to be. Note that this
			// doesn't change the player's position.
//			loc.setY(200);
//			player.teleport(loc);
		}

	}

	static String meteorIp = "localhost";
	static int meteorPort = 3000;
	static String meterUsername = "admin2";
	static String meteorPassword = "asdf";
	static String minecraftPlayerId1 = "GameStartSchool";
	static String minecraftPlayerId2 = "denrei";
	CodeMageDDP ddp = null;

	public String ddp(CommandSender sender) {
		try {
			log("DDP Plugin being invoked from CodeMagePython.");
			ddp = new CodeMageDDP(meteorIp, meteorPort);
			log("DDP Plugin attempting to connect....");
			ddp.connect(meterUsername, meteorPassword);

			String code = "result=98765"; // Main.runWoodenSwordSwingCodeForPlayer(ddp,
											// minecraftPlayerId2);
			System.out.println("FOUND CODE: " + code);
//			executePython(sender, code);

			System.out.println("Shutting down in 3...");
			Thread.sleep(1000);
			System.out.println("2...");
			Thread.sleep(1000);
			System.out.println("1...");
			Thread.sleep(1000);
			ddp.disconnect();
			return "DDP SUCCESS";
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return "DDP Failure.";
	}
}
