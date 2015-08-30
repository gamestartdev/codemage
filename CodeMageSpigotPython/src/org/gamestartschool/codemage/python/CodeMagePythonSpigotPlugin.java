package org.gamestartschool.codemage.python;
import java.net.URISyntaxException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.gamestartschool.codemage.ddp.CodeMageDDP;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class CodeMagePythonSpigotPlugin extends JavaPlugin {

	protected PluginManager pm;

	public void onDisable() {
	}

	public void onEnable() {
		getServer().getLogger().severe("CodeMagePython Enabled");
	}

	@Override
	public void onLoad() {
		getServer().getLogger().severe("CodeMagePython Loaded");
	}

	private void log(String msg) {
		getServer().getLogger().severe(msg);
	}

	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("python")) {
			String code = args[0].toString();
			String ddpResult = ddp(sender);
			log("Operation: " + ddpResult);
		}
		return false;
	}

	private void executePython(CommandSender sender, String code) {
		PythonInterpreter pi = new PythonInterpreter();
		pi.set("i", new PyInteger(42));
		pi.exec(code);
		PyObject pyResult = pi.get("result");
		String resultMessage = pyResult.toString();
		log(resultMessage);
		sender.sendMessage("Result of code: " + resultMessage);
		pi.close();
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
			executePython(sender, code);

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
