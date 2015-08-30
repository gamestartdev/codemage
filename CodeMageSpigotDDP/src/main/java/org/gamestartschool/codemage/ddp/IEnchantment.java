package org.gamestartschool.codemage.ddp;

import java.util.List;

interface IEnchantment {
	public String getName();
	public IEnchantmentBinding getBinding();
	public IEnchantmentTrigger getTrigger();
	public List<ISpell> getSpells();
}
