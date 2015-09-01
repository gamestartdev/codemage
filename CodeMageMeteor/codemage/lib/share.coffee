share.codeMageConstants = {}

share.codeMageConstants.currentVersion = "0.0.1"
share.codeMageConstants.defaultNamespace = "org.gamestartschool.codemage"

share.codeMageConstants.itemMaterials = []
for adj in ['WOOD', 'STONE', 'IRON', 'DIAMOND']
  for tool in ['SWORD', 'AXE', 'PICKAXE', 'HOE', 'SPADE']
    share.codeMageConstants.itemMaterials.push adj + "_" + tool
share.codeMageConstants.actions = ['LEFT_CLICK_AIR', 'LEFT_CLICK_BLOCK', 'PHYSICAL', 'RIGHT_CLICK_AIR', 'RIGHT_CLICK_BLOCK']
share.codeMageConstants.defaultCode = 'yell("OOPS")\ntime.sleep(2)\nspawnentity(player.x, player.y, player.z, "primed_tnt")'