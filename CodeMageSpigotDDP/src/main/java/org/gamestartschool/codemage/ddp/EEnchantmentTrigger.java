package org.gamestartschool.codemage.ddp;


enum EEnchantmentTrigger {
	//Consider this a brainstorm and not a thought-out set of ideas ;)
	
	PRIMARY, 	//Code should be invoked before player LMB with the item
	SECONDARY, 	//Code should be invoked before player RMB with the item
	PREDAMAGE,	//Code should be invoked before damage is applied to the player
	POSTDAMAGE,	//Code should be invoked after damage is applied to the player
	JUMP,		//Code should be invoked before the player jumps
}
