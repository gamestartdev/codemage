def launchentities():
	for entity in getentitieswithselector("@e[type=!Player,r=20]"):
		propel(0, 3, 0, entity)

def witheringray():
	lx = player.lookx
	ly = player.looky
	lz = player.lookz
	for n in range(0, 20):
		entities = getentitiesinrange(2, player.x + n * lx, player.y + n * ly, player.z + n * lz)
		for entity in entities:
			potioneffect(WITHER, 1000, 2, entity)