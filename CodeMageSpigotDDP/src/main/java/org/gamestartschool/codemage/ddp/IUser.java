package org.gamestartschool.codemage.ddp;

import java.util.List;

public interface IUser {
	String getMinecraftUserId();
	List<IEnchantment> getEnchantments();
	void updateHealth(int health);
}