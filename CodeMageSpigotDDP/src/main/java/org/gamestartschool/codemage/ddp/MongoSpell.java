package org.gamestartschool.codemage.ddp;

import java.util.Map;

class MongoSpell extends AMongoDocument implements ISpell {
	private ISpellMeteorMethodCaller meteorCaller;

	public MongoSpell(String id, Map<String, Object> fields, ISpellMeteorMethodCaller methodCaller) {
		super(id, fields);
		this.meteorCaller = methodCaller;
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
}