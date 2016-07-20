settracefunc()
if spellname in xpReqs:
    if not (jplayer.getLevel() >= xpReqs[spellname]):
        senderror("Not enough XP!")
        raise Exception("Not enough XP!")