package org.gamestartschool.codemage.ddp;

import java.util.ArrayList;
import java.util.List;

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
	public List<IEnchantment> getEnchantments() {
		return new ArrayList<IEnchantment>();
	}

	@Override
	public void updateHealth(int health) {
		
	}
}
