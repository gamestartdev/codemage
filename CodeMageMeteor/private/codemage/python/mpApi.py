from org.bukkit import Bukkit
_version = Bukkit.getServer().getClass().getPackage().getName()
_version = _version.split(".")[3]
_nmsPath = "net.minecraft.server." + _version

def _importNms(classname):
    import importlib
    m = getattr(importlib.import_module(_nmsPath), classname)
    return m
    
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
    if isinstance(data, basestring) or isinstance(data, unicode):
        nbt = nbt + '"' + data + '"'
    return nbt

def buildCommand(cmd, args):
    ret = cmd
    for arg in args:
        ret = ret + " " + str(arg)
    return ret

def parseLazyJSON(json):
    print json
    import re
    print "compound check"
    if re.match(r"^{[\s\S]*}$", json):
        #print "compound 1"
        #json = re.sub(r"^{", r"", json)
        #print "compound 2"
        #json = re.sub(r"}$", r"", json)
        asdict = {}
        for pair in json[1:-1].split(","):
            pos = pair.find(":")
            key = pair[:pos]
            value = pair[pos+1:]
            asdict[str(key)] = parseLazyJSON(value)
        return asdict
    elif  re.match(r"^(?:\d|-\d)[\d.]*[LlDdFfBbIiSs]?$", json):
        print "number 1"
        print json, type(json)
        json = re.sub(r"[^\d.]", "", json)
        print json, type(json)
        if "." in json:
            return float(json)
        else:
            return long(json)
    elif printret("string test") and re.match(r"^\"[\s\S]*\"$", json):
        return str(json)[1:-1]
    elif printret("list test") and re.match(r"^\[[\s\S]*\]$", json):
        print "list 1"
        json = re.sub(r"[[,]\d+:", "", json)
        yell("TEST")
        #print "list 2"
        #json = re.sub(r"^\[", "", json)
        #print "list 3"
        #json = re.sub(r"]$", "", json)
        aslist = []
        for value in json[1:-1].split(","):
            aslist.append(parseLazyJSON(value))
        return aslist
    print json
    
def printret(string):
    print string
    return True

def test(x, y, z, entity, nbt):
    import json
    import copy
    print "thing happened"
    NBTTagCompound = _importNms("NBTTagCompound")
    MojangsonParser = _importNms("MojangsonParser")
    MojangsonParseException = _importNms("MojangsonParseException")
    #$print NBTTagCompound.__dict__
    entity = mc(player.getWorld().spawnEntity, loc(x, y, z), entity).getHandle()
    tag = entity.getNBTTag()
    if tag == None:
        tag = NBTTagCompound()
    entity.c(tag)
    print parseLazyJSON(tag.toString())
    startdict = parseLazyJSON(tag.toString())
    print startdict
    '''try:
        startdict.update(nbt)
        print startdict, "startdict"
        mojson = toMojangson(startdict)
        print startdict
        print mojson
        print type(u'')
        tag = MojangsonParser.parse(mojson)
    except MojangsonParseException, e:
        e.printStackTrace()
        print "thank mr skeltal"
        thank = "mr skeltal" #doot doot (Python complains if there isn't code here.)
    mc(entity.f, tag)'''
    
    
    

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