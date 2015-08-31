package org.gamestartschool.codemage.ddp;

import static ch.lambdaj.Lambda.convert;

import java.util.List;
import java.util.Map;

import ch.lambdaj.function.convert.Converter;

class MongoEnchantment extends AMongoDocument implements IEnchantment {
	
	private ICodeMageCollection<MongoSpell> spells;

	public MongoEnchantment(ICodeMageCollection<MongoSpell> spells, String id, Map<String, Object> fields) {
		super(id, fields);
		this.spells = spells;
	}

	@Override
	public String getName() {
		return getStringField("name");
	}

	@Override
	public String userId() {
		return getStringField("userId");
	}
	@Override
	public IEnchantmentBinding getBinding() {
		String bindingString = getStringField("binding");
		return EDummyEnchantmentBinding.valueOf(bindingString.toUpperCase());
	}

	@Override
	public IEnchantmentTrigger getTrigger() {
		String bindingTrigger= getStringField("trigger");
		return EDummyEnchantmentTrigger.valueOf(bindingTrigger.toUpperCase());
	}

	private List<String> getSpellIds() {
		return getStringListField("spellIds");
	}
	
	@Override
	public List<ISpell> getSpells() {
		return convert(getSpellIds(), new Converter<String, ISpell>(){
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