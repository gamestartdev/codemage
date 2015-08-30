package org.gamestartschool.codemage.ddp;

import static ch.lambdaj.Lambda.convert;

import java.util.List;

import ch.lambdaj.function.convert.Converter;

class MongoEnchantment implements IEnchantment, IMongoDocument {
	public final String name;
	public final IEnchantmentTrigger trigger;
	public IEnchantmentBinding binding;
	public List<String> spellIds;
	public String id;
	public String userId;
	private ICodeMageCollection<MongoSpell> spells;
	
	public MongoEnchantment(ICodeMageCollection<MongoSpell> spells, String id, String userId, String name, IEnchantmentBinding binding, IEnchantmentTrigger trigger, List<String> spellIds) {
		this.spells = spells;
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.binding = binding;
		this.trigger = trigger;
		this.spellIds = spellIds;
	}
	
	@Override
	public String toString() {
		return "MongoEnchantment [name=" + name + ", trigger=" + trigger + ", binding=" + binding + ", spells=" + spellIds
				+ ", id=" + id + ", userId=" + userId + "]";
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public IEnchantmentBinding getBinding() {
		return binding;
	}

	@Override
	public IEnchantmentTrigger getTrigger() {
		return trigger;
	}

	@Override
	public List<ISpell> getSpells() {
		return convert(spellIds, new Converter<String, ISpell>(){
			public ISpell convert(String id) {
	                return spells.get(id);
	        }
		});
	}

	@Override
	public String getId() {
		return id;
	}
}