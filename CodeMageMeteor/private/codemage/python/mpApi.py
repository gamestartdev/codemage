def loc(x,y,z):
    from org.bukkit import Location
    return Location(player.getWorld(), x, y, z)

def setblock(x,y,z, mat):
	return mc(loc(x,y,z).getBlock().setType, mat)

def teleport(x,y,z):
	return mc(player.teleport, loc(x,y,z))

def myX():
	return player.getLocation().getX()

def myY():
	return player.getLocation().getY()

def myZ():
	return player.getLocation().getZ()

def lookVector():
    return player.getLocation().getDirection().normalize()

def lookX():
    return lookVector().getX()
    
def lookY():
    return lookVector().getY()
    
def lookZ():
    return lookVector().getZ()

def explosion(x, y, z, power=5):
    mc(player.getWorld().createExplosion, x, y, z, power, False, True)

def yell(message):
	print "Yelling: " + message
	mc(player.chat, message)

def spawnentity(x, y, z, entity, data={}): 
    entity = mc(player.getWorld().spawnEntity, loc(x, y, z), entity)
    cmd = "entitydata " + entity.getUniqueId().toString() + " " + toNbt(data)
    __command(cmd)

def __command(cmd):
    mc(player.getServer().dispatchCommand, player.getServer().getConsoleSender(), cmd)

def isNumber(var):
	try:
		dummy = var - 1
	except TypeError:
		return False
	return True

def toNbt(data, isSelfcalled=False):
	nbt = ""
	if isinstance(data, dict):
		nbt = nbt + "{"
		for key in data.keys():
			nbt = nbt + key + ":" + toNbt(data[key], True) + ","
		if nbt[-1:] == ",":	
			nbt = nbt[:-1] #Remove extra comma
		nbt = nbt + "}"
	elif not isSelfcalled:
		raise TypeError("The outermost NBT tag must be a dictionary!")
	if isNumber(data):
		nbt = nbt + str(data)
	if isinstance(data, list) or isinstance(data, tuple):
		nbt = nbt + "["
		for item in data:
			nbt = nbt + toNbt(item, True) + ","
		if nbt[-1:] == ",":	
			nbt = nbt[:-1] #Remove extra comma
		nbt = nbt + "]"
	if isinstance(data, basestring):
		nbt = nbt + '"' + data + '"'
	return nbt

def spawnitem(x, y, z, item=DIRT, count=1, damage=0, data={}):
    from org.bukkit.inventory import ItemStack
    dictdata = dict()
    dictdata["Item"] = {"Count":count,"Damage":damage,"id":item.toString().lower()}
    dictdata["Item"]["tag"] = data
    strdata = toNbt(dictdata)
    cmd = "summon Item " + str(x) + " " + str(y) + " " + str(z) + " " + strdata
    __command(cmd)