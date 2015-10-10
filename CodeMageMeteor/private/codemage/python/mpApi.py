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

def yell(message):
	print "Yelling: " + message
	mc(player.chat, message)

def spawnentity(x, y, z, entity, data="{}"): 
    entity = mc(player.getWorld().spawnEntity, loc(x, y, z), entity)
    cmd = "entitydata " + entity.getUniqueId().toString() + " " + data
    mc(player.getServer().dispatchCommand, player.getServer().getConsoleSender(), cmd)

def spawnitem(x, y, z, item=DIRT, count=1, damage=0, data="{}"):
    from org.bukkit.inventory import ItemStack
    import json
    dictdata = dict()
    dictdata["Item"] = {"Count":count,"Damage":damage,"id":item.toString().lower()}
    dictdata["Item"]["tag"] = json.loads(data)
    strdata = json.dumps(dictdata)
    #itemstack = ItemStack(item, count, damage)
    #itementity = mc(player.getWorld().dropItem, loc(x, y, z), itemstack)
    cmd = "summon Item " + str(x) + " " + str(y) + " " + str(z) + " " + strdata
    print cmd
    mc(player.getServer().dispatchCommand, player.getServer().getConsoleSender(), cmd)
    #spawnentity(x, y, z, DROPPED_ITEM, strdata)