package org.gamestartschool.codemage.ddp;

import static ch.lambdaj.Lambda.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

import ch.lambdaj.function.matcher.Predicate;

class MongoUser extends AMongoDocument implements IUser {

	private ICodeMageCollection<MongoSpell> spells;
	private ICodeMageCollection<MongoGroup> groups;
	private IUserMeteorMethods meteorMethods;
	private boolean removed;

	public MongoUser(ICodeMageCollection<MongoSpell> spells, ICodeMageCollection<MongoGroup> groups,
			String id, Map<String, Object> fields, IUserMeteorMethods methodCaller) {
		super(id, fields);
		this.spells = spells;
		this.groups = groups;
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

	@Override
	public void removed() {
		removed = true;
	}
	
	@Override
	public IGroup getGroup() {
		Predicate<MongoGroup> groupForUser = new Predicate<MongoGroup>() {
			@Override
			public boolean apply(MongoGroup group) {
				return group.getMemberIds().contains(getId());
			}
		};
		List<MongoGroup> userGroups = filter(groupForUser, groups.getAll());
		if(!userGroups.isEmpty())
		{
			return userGroups.get(0);
		}
		return DefaultGroup.INSTANCE;
	}
	
	@Override
	public List<ISpell> getSpells() {
		if (removed) return new ArrayList<ISpell>();
		
		Predicate<MongoSpell> spellsForUser = new Predicate<MongoSpell>() {
			@Override
			public boolean apply(MongoSpell e) {
				return id.equals(e.userId());
			}
		};
		return filter(spellsForUser, new ArrayList<ISpell>(spells.getAll()));
	}

	@Override
	public List<ISpell> getSpells(final Material material, final Action action) {
		Predicate<MongoSpell> spellsForTriggerBinding = new Predicate<MongoSpell>() {
			@Override
			public boolean apply(MongoSpell e) {
				return material.equals(e.getMaterial()) && action.equals(e.getAction());
			}
		};
		return filter(spellsForTriggerBinding, getSpells());
	}
}
