package org.gamestartschool.codemage.ddp;

import static ch.lambdaj.Lambda.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ch.lambdaj.function.matcher.Predicate;

class MongoUser extends AMongoDocument implements IUser {

	private ICodeMageCollection<MongoSpell> spells;
	private ICodeMageCollection<MongoEnchantment> enchantments;
	private IUserMeteorMethods meteorMethods;

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
		meteorMethods.healthUpdate(id, health);
	}

	public List<IEnchantment> getEnchantments() {
		Collection<? extends IEnchantment> all = enchantments.getAll();
		List<IEnchantment> enchantments = new ArrayList<IEnchantment>(all);
		return filter(new EnchantmentsByPlayer(), enchantments);
	}

	class EnchantmentsByPlayer extends Predicate<MongoEnchantment> {

		@Override
		public boolean apply(MongoEnchantment e) {
			return id.equals(e.userId());
		}

	}

}
