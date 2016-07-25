package org.gamestartschool.codemage.ddp;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

public interface IUser {
	String getMinecraftUserId();
	//List<IEnchantment> getEnchantments();
	//List<IEnchantment> getEnchantments(Material material, Action action);
	List<ISpell> getSpells();
	List<ISpell> getSpells(Material material, Action action);
	String getId();
	IGroup getGroup();
	void updateHealth(int health);
}