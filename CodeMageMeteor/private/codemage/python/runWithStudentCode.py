player = PyPlayer(jplayer)
settracefunc()
jplayrstorage.append(jplayer)
#This seems to be the only part of python where jplayer can be accessed.
#Python is a derp.

if spellname in xpReqs:
    if not (jplayer.getLevel() >= xpReqs[spellname]):
        senderror("Not enough XP!")
        raise Exception("Not enough XP!")