package org.gamestartschool.codemage.python;

public class Dummy {
	private String getCode() {
		String code = "import pdb\n";
//		code += "from org.bukkit.event import EventPriority\n";
//		code += "from org.gamestartschool.codemage.python import CodeMagePythonSpigotPlugin\n";
//		code += "from org.bukkit.entity import Player as _Player\n";
//		code += "import __builtin__\n";
//		code += "__builtin__.__import__, __builtin__.reload = None, None\n";
//		code += "class PlayerWrap():\n";
//		code += "	def __init__(self, p):\n"; //, *args, **kwargs
//		code += "		self.p = p\n";
//		code += "		print 'doing it'\n";
//		code += "	def blink(self):\n";
//		code += "		loc = self.p.getLocation()\n";
//		code += "		print loc\n";
//		code += "		loc.setY(100)\n";
//		code += "		print loc\n";
//		code += "		self.p.teleport(loc)\n";
//		code += "		print 'teleported!'\n";
//		code += "p = PlayerWrap(player)\n";
		
//		code += "loc = player.getLocation()\n";
//		code += "loc.setY(200)\n";
//		code += "player.teleport(loc)\n";
		code += "print 'setting trace...'\n";
		code += "pdb.set_trace()\n";
		code += "for x in range(['carlos', 'matt', 'nate']):\n";
		code += "	print x\n";
//		code += "p.blink()\n";
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
}
