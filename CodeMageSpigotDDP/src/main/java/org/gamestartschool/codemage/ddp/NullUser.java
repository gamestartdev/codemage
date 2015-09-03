package org.gamestartschool.codemage.ddp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

class NullUser implements IUser {

	public static final IUser NULL = new NullUser();
	private NullUser() {}
	
	public String getMinecraftUserId(){
		return "NULL USER";
	}

	public List<ISpell> getSpells(){
		return new ArrayList<ISpell>();
	}

	@Override
	public List<IEnchantment> getEnchantments(Material material, Action action){
		return new ArrayList<IEnchantment>();
	}
	@Override
	public List<IEnchantment> getEnchantments() {
		return new ArrayList<IEnchantment>();
	}
	
	@Override
	public void updateHealth(int health) {
		
	}

	@Override
	public List<ISpell> getGameWrappers() {
		return new ArrayList<ISpell>();
	}


}
