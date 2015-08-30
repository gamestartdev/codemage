share.codeMageConstants = {}

share.codeMageConstants.currentVersion = "0.0.1"
share.codeMageConstants.defaultNamespace = "org.gamestartschool.codemage"

share.codeMageConstants.bindings = []
for adj in ['wooden', 'stone', 'iron', 'diamond']
  for tool in ['sword', 'axe', 'pickaxe']
    share.codeMageConstants.bindings.push adj + "_" + tool
share.codeMageConstants.triggers = ['primary', 'secondary', 'predamage', 'postdamage', 'jump']
share.codeMageConstants.defaultCode = 'yell("OOPS")\ntime.sleep(2)\nspawnentity(player.x, player.y, player.z, "primed_tnt")'