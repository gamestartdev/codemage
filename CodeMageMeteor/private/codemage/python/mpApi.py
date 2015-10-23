#If you're here because ImportErrors started happening after a server update:
#change the import below to v(major minecraft version, replace . with _)
#_R(spigot revision)
from org.bukkit import Bukkit
_version = Bukkit.getServer().getClass().getPackage().getName()
_version = _version.split(".")[3]
_nmsPath = "net.minecraft.server." + _version

def _importNms(classname):
    import importlib
    return importlib.import_module( _nmsPath).__dict__[classname]
    
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
    _command(cmd)

def _command(cmd):
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

def toMojangson(data, isSelfcalled=False):
	nbt = ""
	if isinstance(data, dict):
		nbt = nbt + "{"
		for key in data.keys():
			nbt = nbt + key + ":" + toMojangson(data[key], True) + ","
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
			nbt = nbt + toMojangson(item, True) + ","
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

def strictifyJSON(j):
    import re #Python hates non--strict json. Minecraft hates strict json.
    j = re.sub(r"{\s*'?(\w)", r'{"\1', j)
    print 1
    j = re.sub(r",\s*'?(\w)", r',"\1', j)
    print 2
    j = re.sub(r"(\w)'?\s*:", r'\1":', j)
    print 3
    j = re.sub(r":\s*'(\w+)'\s*([,}])", r':"\1"\2', j)
    print 4
    j = re.sub(r"(:[)\"?\d\":", r'\1', j)
    print 5
    #j = re.sub(r"\"\d\":", r)
    return j
    

def test(x, y, z, entity, nbt):
    import json
    NBTTagCompound = _importNms("NBTTagCompound")
    MojangsonParser = _importNms("MojangsonParser")
    MojangsonParseException = _importNms("MojangsonParseException")
    print NBTTagCompound.__dict__
    entity = mc(player.getWorld().spawnEntity, loc(x, y, z), entity).getHandle()
    tag = entity.getNBTTag()
    if tag == None:
        tag = NBTTagCompound()
    entity.c(tag)
    print strictifyJSON(tag.toString())
    startdict = json.loads(strictifyJSON(tag.toString()))
    try:
        startdict.update(nbt)
        tag = MojangsonParser.parse(toMojangson(startdict))
    except MojangsonParseException:
        thank = "mr skeltal" #doot doot (Python complains if there isn't code here.)
    entity.f(tag)
    
    
    

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
    _command(cmd)