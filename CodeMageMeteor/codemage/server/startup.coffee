preCode = """
def mc(method, *args):
    from org.gamestartschool.codemage.python import PythonMethodCall
    import time
    methodCall = PythonMethodCall(method, args)
    #This variable injected from Java
    pythonMethodQueue.offer(methodCall)
    #Maybe better to statically reference?
    while not methodCall.isDone():
        time.sleep(.01)
    return methodCall.get()

def loc(x,y,z):
    from org.bukkit import Location
    return Location(player.getWorld(), x, y, z)

def setblock(x,y,z, mat):
	return mc(loc(x,y,z).getBlock().setType, mat)

def teleport(x,y,z):
	return mc(player.teleport, loc(x,y,z))

def myX():
	return mc(player.getLocation).getX()

def myY():
	return mc(player.getLocation).getY()

def myZ():
	return mc(player.getLocation).getZ()

from org.bukkit.Material import *

ECHO = True
def trace_function(frame, event, arg):
    method_name = frame.f_code.co_name
    if ECHO and event == 'call' and method_name != 'mc':
        frame = frame.f_back or frame
        print "LINE: %i: %s" % (frame.f_lineno, method_name)
    return trace_function
import sys
sys.settrace(trace_function)
"""

Meteor.startup ->
  console.log "CodeMage Module Startup"
  defaultGameRulesSpell = 'DefaultGameRules'
  defaultTome = 'GameRules'

  spells.remove { name: defaultGameRulesSpell}
  tomes.remove { name: defaultTome}

  if spells.find(defaultGameRulesSpell).count() == 0
    if tomes.find(defaultTome).count() == 0
      tomeId = tomes.insert
        _id: defaultTome
        name: defaultTome
        userId: Meteor.users.findOne({username:'admin'})._id

    spellId = spells.insert
      _id: defaultGameRulesSpell
      tomeId: tomeId
      name: defaultGameRulesSpell
      code: preCode
      message: ""
      status: "creating"
      preprocess: true
      version: share.codeMageConstants.currentVersion
      namespace: share.codeMageConstants.defaultNamespace