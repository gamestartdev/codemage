package org.gamestartschool.codemage.ddp;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

class MongoSpell extends AMongoDocument implements ISpell {
	private ISpellMeteorMethodCaller meteorCaller;

	public MongoSpell(String id, Map<String, Object> fields, ISpellMeteorMethodCaller methodCaller) {
		super(id, fields);
		this.meteorCaller = methodCaller;
	}
	
	@Override
	public String userId() {
		return getStringField("userId");
	}
	
	@Override
	public String getCode() {
		return getStringField("code");
	}

	@Override
	public String getName() {
		return getStringField("name");
	}
	
	@Override
	public boolean getStatus() {
		return getBooleanField("status");
	}

	@Override
	public void changed(Map<String, Object> fields) {
		super.changed(fields);
		if("executeRequest".equals(getStatus())) {
		}
	}

	@Override
	public void removed() {
	}

	public boolean isGameWrapper() {
		return getBooleanField("preprocess");
	}

	@Override
	public Action getAction() {
		return Action.valueOf(getStringField("action"));
	}

	@Override
	public Material getMaterial() {
		System.out.println(toString());
		return Material.valueOf(getStringField("itemMaterial"));
	}
}