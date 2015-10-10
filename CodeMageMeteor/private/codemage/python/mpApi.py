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

def toNBT(thing):
    from net.minecraft.server.v1_8_R3 import NBTTagCompound, NBTTagList, NBTTagInt, \
    NBTTagLong, NBTTagByteArray, NBTTagIntArray, NBTTagLong, NBTTagShort, NBTTagByte, \
    NBTTagString #don't show in eclipse for some reason
    if isinstance(thing, basestring):
        return NBTTagString(thing)

def spawnentity(x, y, z, entity, data={}): #compound/list tags not implemented
    import jarray #                        #arrays of ints/bytes work, though
    from net.minecraft.server.v1_8_R3 import NBTTagCompound #doesn't show up in eclipse
    entity = mc(player.getWorld().spawnEntity, loc(x, y, z), entity)
    entityhandle = entity.getHandle()
    tag = entityhandle.getNBTTag()
    if tag == None:
        tag = NBTTagCompound()
    mc(entityhandle.c, tag)
    for tagname in data:
        value = data[tagname]
        if isinstance(value, basestring):
            tag.setString(tagname, value)
        elif isinstance(value, int):
            tag.setInt(tagname, value)
        elif isinstance(value, float):
            tag.setFloat(tagname, value)
        elif isinstance(value, long):
            tag.setLong(tagname, value)
        elif isinstance(value, list):
            isints = True
            for e in value:
                if not isinstance(int, e):
                    isints = False
            if isints:
                isbytes = True
                for e in value:
                    if not isinstance(int, e) and e in range(-128, 128):
                        isbytes = False
                if isbytes:
                    javaarray = jarray.array(value, b)
                    tag.setByteArray(tagname, javaarray)
                else:
                    javaarray = jarray.array(value, i)
                    tag.setIntArray(tagname, javaarray)
    mc(entityhandle.f, tag)
