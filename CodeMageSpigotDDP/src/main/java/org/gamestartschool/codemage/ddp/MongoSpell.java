package org.gamestartschool.codemage.ddp;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

class MongoSpell extends AMongoDocument implements ISpell {

	public MongoSpell(String id, Map<String, Object> fields) {
		super(id, fields);
	}
	
	@Override
	public String userId() {
		return getStringField("userId");
	}
	
	@Override
	public String getCode() {
		return getStringField("code");
	}
	
	public boolean isEnabledWrapper() {
		return getBooleanField("wrapperEnabled");
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
	}

	@Override
	public void removed() {
	}

	public boolean isGameWrapper() {
		System.out.println(getName());
		return getBooleanField("wrapper");
	}
	
	public boolean isLibrary() {
		return getBooleanField("library");
	}

	@Override
	public Action getAction() {
		return Action.valueOf(getStringField("action"));
	}

	@Override
	public Material getMaterial() {
		System.out.println(getStringField("itemMaterial"));
		System.out.println(getName());
		if(getStringField("itemMaterial").equals("DISABLED"))
		{
			return Material.FIRE;
		}
		return Material.valueOf(getStringField("itemMaterial"));
	}

	public int getWrapperPriority() {
		return Integer.parseInt(getStringField("wrapperPriority"));
	}
}