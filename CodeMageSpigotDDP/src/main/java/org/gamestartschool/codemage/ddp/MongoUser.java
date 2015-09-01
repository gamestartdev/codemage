package org.gamestartschool.codemage.ddp;

import static ch.lambdaj.Lambda.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

import ch.lambdaj.function.matcher.Predicate;

class MongoUser extends AMongoDocument implements IUser {

	private ICodeMageCollection<MongoSpell> spells;
	private ICodeMageCollection<MongoEnchantment> enchantments;
	private IUserMeteorMethods meteorMethods;
	private boolean removed;

	public MongoUser(ICodeMageCollection<MongoSpell> spells, ICodeMageCollection<MongoEnchantment> enchantments,
			String id, Map<String, Object> fields, IUserMeteorMethods methodCaller) {
		super(id, fields);
		this.spells = spells;
		this.enchantments = enchantments;
		meteorMethods = methodCaller;
	}

	@Override
	public String getMinecraftUserId() {
		return getStringField("minecraftPlayerId");
	}

	public void updateHealth(int health) {
		if (removed)
			return;
		meteorMethods.healthUpdate(id, health);
	}

	public List<IEnchantment> getEnchantments() {
		if (removed) return new ArrayList<IEnchantment>();
		
		Predicate<MongoEnchantment> enchantmentsForUser = new Predicate<MongoEnchantment>() {
			@Override
			public boolean apply(MongoEnchantment e) {
				return id.equals(e.userId());
			}
		};
		return filter(enchantmentsForUser, new ArrayList<IEnchantment>(enchantments.getAll()));
	}

	@Override
	public List<IEnchantment> getEnchantments(final Material material, final Action action) {
		Predicate<MongoEnchantment> enchantmentsForTriggerBinding = new Predicate<MongoEnchantment>() {
			@Override
			public boolean apply(MongoEnchantment e) {
				return material.equals(e.getMaterial()) && action.equals(e.getAction());
			}
		};
		return filter(enchantmentsForTriggerBinding, getEnchantments());
	}

	class EnchantmentsByPlayer extends Predicate<MongoEnchantment> {

		@Override
		public boolean apply(MongoEnchantment e) {
			return id.equals(e.userId());
		}
	}

	@Override
	public void removed() {
		removed = true;
	}

}
