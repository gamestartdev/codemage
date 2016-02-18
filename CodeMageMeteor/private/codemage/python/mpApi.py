from org.bukkit import Bukkit
_version = Bukkit.getServer().getClass().getPackage().getName()
_version = _version.split(".")[3]
_nmsPath = "net.minecraft.server." + _version
_craftPath = "org.bukkit.craftbukkit." + _version + "."
Bukkit = None

'''AttributeError: import_module does not exist investigation:
Not fixed by complete replace of server files
Not fixed by reloads
Possibly fixed by calling importlib.__init__()'''

def senderror(message):
    from org.bukkit import Bukkit
    CraftPlayer = _importCraft("entity", "CraftPlayer")
    jplayer.sendMessage(message)

import math

class LessPickyMath(math):
    
    def __getattribute__(self, attr):
        if "getClass" not in attr and "__class__" not in attr:
            return object.__getattribute__(self, attr)
        else:
            raise AttributeError("Non existant attribute")
            
    def __setattr__(self, attr, val):
        raise AttributeError("Non existant attribute")

class PyEntityBase(object):
    
    def __init__(self, javaversion):
        object.__setattr__(self, "javaversion", javaversion)
    
    @property
    def x(self):
        return object.__getattribute__(self, "javaversion").getLocation().getX()
    
    @property
    def y(self):
        return object.__getattribute__(self, "javaversion").getLocation().getY()
    
    @property
    def z(self):
        return object.__getattribute__(self, "javaversion").getLocation().getZ()
    
    @property
    def lookx(self):
        return object.__getattribute__(self, "javaversion").getLocation().getDirection().normalize().getX()
    
    @property
    def looky(self):
        return object.__getattribute__(self, "javaversion").getLocation().getDirection().normalize().getY()
    
    @property
    def lookz(self):
        return object.__getattribute__(self, "javaversion").getLocation().getDirection().normalize().getZ()
        
    def __getattribute__(self, attr):
        if "getClass" not in attr and "__class__" not in attr and "javaversion" not in attr:
            return object.__getattribute__(self, attr)
        else:
            raise AttributeError("Non existant attribute")
            
    def __setattr__(self, attr, val):
        raise AttributeError("Non existant attribute")

class FakeTime(object):
    
    def __getattribute__(self, attr):
        if "getClass" not in attr and "__class__" not in attr:
            return object.__getattribute__(self, attr)
        else:
            raise AttributeError("Non existant attribute")
            
    def __setattr__(self, attr, val):
        raise AttributeError("Non existant attribute")
    
    def sleep(self, length):
        if length <= 5:
            from time import sleep as realsleep
            realsleep(length)
        else:
            raise Exception("You cannot time.sleep for more than 5 seconds!")
            
class PyEntity(PyEntityBase):
    
    def __getattribute__(self, attr):
        if "getClass" not in attr and "__class__" not in attr and "javaversion" not in attr:
            return object.__getattribute__(self, attr)
        else:
            raise AttributeError("Non existant attribute")
            
    def __setattr__(self, attr, val):
        raise AttributeError("Non existant attribute")
    
    def kill(self):
        javaversion = object.__getattribute__(self, "javaversion")
        mc_fast(javaversion.world.kill, javaversion)
    
    def getHealth(self):
        return object.__getattribute__(self, "javaversion").getHealth()
    
    def setHealth(self, health):
        mc_fast(object.__getattribute__(self, "javaversion").setHealth, health)
    
    def heal(self, howmuch):
        mc_fast(object.__getattribute__(self, "javaversion").setHealth, self.getHealth() + howmuch)
    
    def damage(self, howmuch):
        mc_fast(object.__getattribute__(self, "javaversion").setHealth, self.getHealth() - howmuch)

'''Python implementation of a player'''
class PyPlayer(PyEntity):
    
    def kill(self):
        self.setHealth(0)
    
    def __getattribute__(self, attr):
        if "getClass" not in attr and "__class__" not in attr and "javaversion" not in attr:
            return object.__getattribute__(self, attr)
        else:
            raise AttributeError("Non existant attribute")
            
    def __setattr__(self, attr, val):
        raise AttributeError("Non existant attribute")
        
    def getName(self):
        return object.__getattribute__(self, "javaversion").getPlayerListName()

time = FakeTime()
player = PyPlayer(jplayer)

def _importNms(classname):
    import importlib
    m = getattr(importlib.import_module(_nmsPath), classname)
    return m
    
def _importCraft(package, classname):
    import importlib
    m = getattr(importlib.import_module(_craftPath + package), classname)
    return m

def cube(x, y, z, size, block):
    x = int(math.floor(x))
    y = int(math.floor(y))
    z = int(math.floor(z))
    for setx in range(x, x + size):
        for sety in range(y, y + size):
            for setz in range(z, z + size):
                setblock(setx, sety, setz, block)

def killall():
    for entity in jplayer.getWorld().getEntities():
        if entity.__class__ != jplayer.__class__:
            mc_fast(entity.remove)

def loc(x,y,z):
    from org.bukkit import Location
    return Location(jplayer.getWorld(), x, y, z)

def setblock(x,y,z, mat):
    x = int(math.floor(x))
    y = int(math.floor(y))
    z = int(math.floor(z))
    mc_fast(loc(x,y,z).getBlock().setType, mat)

def teleport(x,y,z):
    mc_fast(jplayer.teleport, loc(x,y,z))

def myX():
    return jplayer.getLocation().getX()

def myY():
    return jplayer.getLocation().getY()

def myZ():
    return jplayer.getLocation().getZ()

def lookVector():
    return jplayer.getLocation().getDirection().normalize()

def lookX():
    return lookVector().getX()
    
def lookY():
    return lookVector().getY()
    
def lookZ():
    return lookVector().getZ()

def explosion(x, y, z, power=5):
    mc_fast(jplayer.getWorld().createExplosion, x, y, z, power, False, True)

def yell(message):
    from time import time as timestamp
    from org.gamestartschool.codemage.python import StaticVariableStorage
    print "Yelling: " + message
    def setit(key, val):
        StaticVariableStorage.antispam.put(key, val)
    if not StaticVariableStorage.antispam.containsKey(jplayer.getPlayerListName()):
        mc(setit, jplayer.getPlayerListName(), 0)
    laststamp = StaticVariableStorage.antispam.get(jplayer.getPlayerListName())
    if timestamp() - laststamp > 3:
        if len(message) > 100:
            senderror("Yells can only be 100 characters long!")
            return
        mc_fast(jplayer.chat, message)
        mc_fast(setit, jplayer.getPlayerListName(), timestamp())
    else:
        senderror("You must wait 3 seconds between yells")

def isNumber(var):
    try:
        dummy = var - 1
    except TypeError:
        return False
    return True

def lightning(x, y, z):
    mc_fast(jplayer.getWorld().strikeLightning, loc(x, y, z))

def getplayernames():
    from org.bukkit import Bukkit
    jplayers = Bukkit.getOnlinePlayers()
    jplayernames = []
    for jplayer in jplayers:
        jplayernames.append(str(jplayer.getDisplayName()))
    return jplayernames

def getblock(x, y, z):
    mat = jplayer.getWorld().getBlockAt(loc(x, y, z)).getType()
    mst = mat.toString()
    if mat != None:
        return LAVA if "LAVA" in mst else WATER if "WATER" in mst else mat
    else:
        return AIR

def potioneffect(effect, duration=10, amplifier=1, target=player):
    MobEffect = _importNms("MobEffect")
    if object.__getattribute__(target, "__class__") == PyPlayer:
        target = object.__getattribute__(player, "javaversion").getHandle()
    else:
        target = object.__getattribute__(target, "javaversion")
    mobeffect = MobEffect(effect.getId(), duration, amplifier - 1, False, True)
    mc_fast(target.addEffect, mobeffect)

def propel(x, y, z, target=player):
    if object.__getattribute__(target, "__class__") == PyPlayer:
        from org.bukkit.util import Vector
        Entity = _importNms("EntityLiving")
        target = object.__getattribute__(target, "javaversion")
        vec = Vector(x, y, z)
        mc_fast(target.setVelocity, vec)
    else:
        target = object.__getattribute__(target, "javaversion")
        mc_fast(target.g, x, y, z)
    
def playsound(x, y, z, sound,pitch=1,volume=1):
    mc_fast(jplayer.getWorld().playSound,loc(x,y,z),sound,1,1)

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

def spawnentity(x, y, z, entitytype, nbt={}):
    if entitytype == ENDER_DRAGON:
        yell(jplayer.getPlayerListName() + "is spawning dragons!")
    NBTTagCompound = _importNms("NBTTagCompound")
    MojangsonParser = _importNms("MojangsonParser")
    craftentity = mc(jplayer.getWorld().spawnEntity, loc(x, y, z), entitytype)
    entity = craftentity.getHandle()
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
    mc_fast(entity.f, tag)
    return PyEntity(entity) 

def spawnparticle(x, y, z, particle, howMany, speed=0, xd=0.5, yd=0.5,zd=0.5):
    mc_fast(jplayer.getWorld().spigot().playEffect,loc(x,y,z),particle,0,0,xd,yd,zd,speed,
    howMany,16)

def spawnitem(x, y, z, item=DIRT, count=1, damage=0, data={}):
    CraftItemStack = _importCraft("inventory", "CraftItemStack")
    ItemStack = _importNms("ItemStack")
    MojangsonParser = _importNms("MojangsonParser")
    itemid = str(item).lower()
    try:
        itemid = spigot_names[itemid]
    except Exception:
        pass
    dictdata = {"Count":count,"Damage":damage,"id":itemid,"tag":data}
    tag = MojangsonParser.parse(toMojangson(dictdata))
    itemStack = ItemStack.createStack(tag)
    mc_fast(jplayer.getWorld().dropItem, loc(x,y,z), CraftItemStack.asCraftMirror(itemStack))

def denyattribute(*args):
    raise AttributeError("Non existant attribute")

def getentitieswithselector(selector):
    return getplayerswithselector(selector)

def getplayerswithselector(selector):
    CommandAbstract = _importNms("CommandAbstract")
    players = CommandAbstract.c(jplayer.getHandle(), selector)
    pyplayers = []
    for aplayer in players:
        if isinstance(aplayer, jplayer.getHandle().__class__):
            from org.bukkit import Bukkit
            CraftPlayer = _importCraft("entity", "CraftPlayer")
            aplayer = CraftPlayer(Bukkit.getServer(), aplayer)
            pyplayers.append(PyPlayer(aplayer))
        else:
            pyplayers.append(PyEntity(aplayer))
    return pyplayers

def getopponent():
    return getplayerswithselector("@p[name=!" + player.getName() + ",m=0,r=200]")[0]

def getentitiesinrange(radius, x=player.x, y=player.y, z=player.z, etype=None):
    if radius in range(0, 101) and etype == None:
        return getentitieswithselector("@e[r=" + str(radius) + "]")
    elif radius in range(0, 101) and etype != None:
        return getentitieswithselector("@e[r="+str(radius)+",type=" +str(etype)+"]")

def randint(lower, upper):
    from random import randint
    return randint(lower, upper)

replaceentity = True
replacematerial = True
replacesound = True
replaceeffect = True

math = LessPickyMath()

try:
    dummy = ZOMBIE.__class__
except Exception:
    replaceentity = False
try:
    dummy = SAND.__class__
except Exception:
    replacematerial = False
try:
    dummy = BAT_DEATH.__class__
except Exception:
    replacesound = False
try:
    dummy = CLICK1.__class__
except Exception:
    replaceeffect = False
    
if replaceentity:
    ZOMBIE.__class__.__getattribute__ = denyattribute
if replacematerial:
    SAND.__class__.__getattribute__ = denyattribute
if replacesound:
    BAT_DEATH.__class__.__getattribute__ = denyattribute
if replaceeffect:
    CLICK1.__class__.__getattribute__ = denyattribute

#Spigot doesn't always call things their item id in the enums
spigot_names = {
    "ender_portal_frame":"end_portal_frame",
    "ender_portal":"end_portal",
    "iron_barding":"iron_horse_armor", #Really, Spigot?
    "gold_barding":"gold_horse_armor", #It's not called barding anywhere in game.
    "diamond_barding":"diamond_horse_armor",
    "explosive_minecart":"tnt_minecart",
    "hard_clay":"hardened_clay",
    "ink_sack":"dye",
    "wood_spade":"wood_shovel",
    "stone_spade":"stone_shovel",
    "iron_spade":"iron_shovel",
    "gold_spade":"gold_shovel",
    "diamond_spade":"diamond_shovel",
    "iron_fence":"iron_bars",
    "leash":"lead",
    "long_grass":"tallgrass",
    "monster_eggs":"spawn_egg",
    "mycel":"mycelium", #Come ON spigot, it's only 3 letters
    "rails":"rail",
    "redstone_lamp_on":"redstone_lamp",
    "redstone_lamp_off":"redstone_lamp",
    "snow_ball":"snowball",
    "storage_minecart":"chest_minecart",
    "sulphur":"gunpowder",
    "workbench":"crafting_table",
    "diode":"repeater",
    "diode_block_off":"repeater",
    "diode_block_on":"repeater"
}

REDSTONE_REPEATER = DIODE_BLOCK_OFF
MYCELIUM = MYCEL
HARDENED_CLAY = HARD_CLAY
WOOD_SHOVEL = WOOD_SPADE
STONE_SHOVEL = STONE_SPADE
IRON_SHOVEL = IRON_SPADE
GOLD_SHOVEL = GOLD_SPADE
DIAMOND_SHOVEL = DIAMOND_SPADE
REDSTONE_LAMP = REDSTONE_LAMP_OFF
TNT_MINECART = EXPLOSIVE_MINECART
CRAFTING_TABLE = WORKBENCH
IRON_BARS = IRON_FENCE
IRON_HORSE_ARMOR = IRON_BARDING
GOLD_HORSE_ARMOR = GOLD_BARDING
DIAMOND_HORSE_ARMOR = DIAMOND_BARDING
DYE = INK_SACK
GUNPOWDER = SULPHUR
CHEST_MINECART = STORAGE_MINECART
END_STONE = ENDER_STONE

__builtins__ = None
globals = None
locals = None
eval = None
#dir = None
compile = None
vars = None
raw_input = None
input = None
open = None
type = None
FakeTime = None
#PyPlayer = None
DenyingRandom = None
LessPickyMath = None
file = None