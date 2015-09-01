package org.gamestartschool.codemage.ddp;

import static ch.lambdaj.Lambda.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

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
	public Material getMaterial() {
		String bindingString = getStringField("itemMaterial");
		return Material.valueOf(bindingString.toUpperCase());
	}

	@Override
	public Action getAction() {
		String bindingTrigger= getStringField("action");
		return Action.valueOf(bindingTrigger.toUpperCase());
	}

	private List<String> getSpellIds() {
		return getStringListField("spellIds");
	}
	
	@Override
	public List<ISpell> getSpells() {
		if(removed) {
			System.out.println("Trying to get spells from a removed enchantment!! "+ id);
			return new ArrayList<ISpell>();
		}
		
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

	private boolean removed = false;
	@Override
	public void removed() {
		removed = true;
	}

}