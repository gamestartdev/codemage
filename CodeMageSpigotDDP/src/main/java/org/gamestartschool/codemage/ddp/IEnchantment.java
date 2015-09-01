package org.gamestartschool.codemage.ddp;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

public interface IEnchantment {
	public String getName();
	public Material getMaterial();
	public Action getAction();
	public List<ISpell> getSpells();
	public String userId();
}
