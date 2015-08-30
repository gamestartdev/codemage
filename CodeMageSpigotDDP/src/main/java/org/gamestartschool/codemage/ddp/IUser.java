package org.gamestartschool.codemage.ddp;

import java.util.List;

interface IUser {
	String getMinecraftUserId();
	List<IEnchantment> getEnchantments();
	void updateHealth(int health);
}