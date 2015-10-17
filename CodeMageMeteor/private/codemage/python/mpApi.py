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

def getblock(x, y, z):
    mat = player.getWorld().getBlockAt(loc(x, y, z)).getType()
    mst = mat.toString()
    if mat != None:
        return LAVA if "LAVA" in mst else WATER if "WATER" in mst else mat
    else:
        return AIR

def playsound(x, y, z, sound,pitch=1,volume=1):
    mc(player.getWorld().playSound,loc(x,y,z),sound,1,1)

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

def buildCommand(cmd, args):
    ret = cmd
    for arg in args:
        ret = ret + " " + str(arg)
    return ret

def spawnparticle(x, y, z, particle, howMany, speed=0, xd=0.5, yd=0.5,zd=0.5):
    mc(player.getWorld().spigot().playEffect,loc(x,y,z),particle,0,0,xd,yd,zd,speed,
    howMany,16)

def spawnitem(x, y, z, item=DIRT, count=1, damage=0, data={}):
    from org.bukkit.inventory import ItemStack
    dictdata = dict()
    dictdata["Item"] = {"Count":count,"Damage":damage,"id":item.toString().lower()}
    dictdata["Item"]["tag"] = data
    strdata = toNbt(dictdata)
    cmd = buildCommand("summon Item", [str(x), str(y), str(z), strdata])
    __command(cmd)