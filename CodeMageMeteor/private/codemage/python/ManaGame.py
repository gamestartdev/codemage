def manaHandler(event):
    from org.gamestartschool.codemage.python import StaticVariableStorage
    import time
    import math
    if "mana" not in StaticVariableStorage.variables:
        StaticVariableStorage.variables["mana"] = {}
    if jplayer.getName() not in StaticVariableStorage.variables["mana"]:
        StaticVariableStorage.variables["mana"][jplayer.getName()] = {}
        playerMana = StaticVariableStorage.variables["mana"][jplayer.getName()]
        playerMana["amount"] = 3
        playerMana["lastUpdated"] = time.time()
    playerMana = StaticVariableStorage.variables["mana"][jplayer.getName()]
    increase = time.time() - playerMana["lastUpdated"]
    if increase >= 1:
        playerMana["amount"] += math.floor(increase)
        playerMana["lastUpdated"] = time.time()
    if playerMana["amount"] > 3:
        playerMana["amount"] = 3
    print playerMana["amount"]
    if playerMana["amount"] >= 1:
        playerMana["amount"] = playerMana["amount"] - 1
    else:
        event.setCanceled(True)
registerEventHandler(manaHandler, setblockEventHandlers)