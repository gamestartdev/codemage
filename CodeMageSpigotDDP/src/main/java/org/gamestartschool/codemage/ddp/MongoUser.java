package org.gamestartschool.codemage.ddp;


import static ch.lambdaj.Lambda.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.lambdaj.function.matcher.Predicate;


class MongoUser implements IMongoDocument, IUser {

	private String id;
	private String meteorUsername;
	private String minecraftPlayerId;
	private String role;
	private ICodeMageCollection<MongoSpell> spells;
	private ICodeMageCollection<MongoEnchantment> enchantments;
	private IUserMeteorMethods meteorMethods;

	public MongoUser(
			ICodeMageCollection<MongoSpell> spells,
			ICodeMageCollection<MongoEnchantment> enchantments, 
			String id, 
			String meteorUsername, String minecraftPlayerId, String role, IUserMeteorMethods meteorMethods) {
		this.meteorMethods = meteorMethods;
		this.spells = spells;
		this.enchantments = enchantments;
		this.id = id;
		this.meteorUsername = meteorUsername;
		this.minecraftPlayerId = minecraftPlayerId;
		this.role = role;
	}

	@Override
	public String getMinecraftUserId() {
		return minecraftPlayerId;
	}
	
	public void updateHealth(int health){
		meteorMethods.healthUpdate(id, health);
	}
	
	@Override
	public String toString() {
		return "MongoUser [id=" + id + ", meteorUsername=" + meteorUsername + ", minecraftPlayerId=" + minecraftPlayerId
				+ ", role=" + role + "]";
	}

	@Override
	public String getId() {
		return id;
	}
	
	public List<IEnchantment> getEnchantments() {
		Collection<? extends IEnchantment> all = enchantments.getAll();
		List<IEnchantment> enchantments = new ArrayList<IEnchantment>(all);
		return filter(new EnchantmentsByPlayer(), enchantments);
	}

	class EnchantmentsByPlayer extends Predicate<MongoEnchantment> {

		@Override
		public boolean apply(MongoEnchantment e) {
			return id.equals(e.userId);
		}
		
	}
	
}
