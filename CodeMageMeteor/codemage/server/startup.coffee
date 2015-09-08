baseSpellCode = "import sys\n";
baseSpellCode += "from org.gamestartschool.codemage.python import CodeRunner\n";
baseSpellCode += "def trace_function(frame, event, arg):\n";
baseSpellCode += "	CodeRunner.traceFunction()\n";
baseSpellCode += "	return trace_function\n";
baseSpellCode += "def console(cmd):\n";
baseSpellCode += "	CodeRunner.console(player, cmd)\n\n";
baseSpellCode += "def teleport(x, y, z):\n";
baseSpellCode += "	CodeRunner.console(player, 'tp %s %s %s' % (x,y,z))\n\n";
baseSpellCode += "def setblock(x, y, z, type):\n";
baseSpellCode += "	CodeRunner.console(player, 'setblock %s %s %s %s' % (x,y,z, type.lower()))\n\n";
baseSpellCode += "sys.settrace(trace_function)\n";

Meteor.startup ->
  console.log "CodeMage Module Startup"
#  Meteor.call ''
#  spells.update 'baseSpell', baseSpellCode