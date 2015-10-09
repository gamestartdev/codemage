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
	
import time
def spawnentity(x, y, z, entity, data="{}"):
    entity = mc(player.getWorld().spawnEntity, loc(x, y, z), entity)
    yell("/entitydata " + entity.getUniqueId().toString() + " " + data)
