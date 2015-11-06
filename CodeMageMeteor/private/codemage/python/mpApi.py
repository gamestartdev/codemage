from org.bukkit import Bukkit
_version = Bukkit.getServer().getClass().getPackage().getName()
_version = _version.split(".")[3]
_nmsPath = "net.minecraft.server." + _version
_craftPath = "org.bukkit.craftbukkit." + _version + "."

def _importNms(classname):
    import importlib
    m = getattr(importlib.import_module(_nmsPath), classname)
    return m
    
def _importCraft(package, classname):
    import importlib
    m = getattr(importlib.import_module(_craftPath + package), classname)
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

def spawnentity(x, y, z, entity, nbt={}):
    NBTTagCompound = _importNms("NBTTagCompound")
    MojangsonParser = _importNms("MojangsonParser")
    entity = mc(player.getWorld().spawnEntity, loc(x, y, z), entity).getHandle()
    tag = entity.getNBTTag()
    if tag == None:
        tag = NBTTagCompound()
    entity.c(tag)
    tagclass = tag.getClass() #Reflection, because there's no getmap or setmap.
    mapField = tagclass.getDeclaredField("map")
    mapField.setAccessible(True)
    entityMap = mapField.get(tag)
    suppliedTag = MojangsonParser.parse(toMojangson(nbt))
    suppliedMap = mapField.get(suppliedTag)
    entityMap.putAll(suppliedMap)  #Merges the tag maps.
    mapField.set(tag, entityMap)
    mc(entity.f, tag)

def spawnparticle(x, y, z, particle, howMany, speed=0, xd=0.5, yd=0.5,zd=0.5):
    mc(player.getWorld().spigot().playEffect,loc(x,y,z),particle,0,0,xd,yd,zd,speed,
    howMany,16)

def spawnitem(x, y, z, item=DIRT, count=1, damage=0, data={}):
    CraftItemStack = _importCraft("inventory", "CraftItemStack")
    ItemStack = _importNms("ItemStack")
    MojangsonParser = _importNms("MojangsonParser")
    dictdata = dict()
    dictdata = {"Count":count,"Damage":damage,"id":item.toString().lower(),"tag":data}
    tag = MojangsonParser.parse(toMojangson(dictdata))
    itemStack = ItemStack.createStack(tag)
    mc(player.getWorld().dropItem, loc(x,y,z), CraftItemStack.asCraftMirror(itemStack))