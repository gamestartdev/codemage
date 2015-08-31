package org.gamestartschool.codemage.ddp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MongoSpell extends AMongoDocument implements ISpell {
	private ISpellMeteorMethodCaller meteorCaller;
	private List<ISpellObserver> observers = new ArrayList<ISpellObserver>();

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
	public void setSpellMessage(String string) {
		meteorCaller.spellMessage(id, string);
	}

	@Override
	public void addObserver(ISpellObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void setStatus(boolean status) {
		meteorCaller.spellStatus(id, status);		
	}

	@Override
	public boolean getStatus() {
		return getBooleanField("status");
	}

	public void NotifyStatusChanged(boolean status) {
		if(status){
			for (ISpellObserver observer : observers) {
				observer.requestCodeExecutionFromBrowser(this);
			}
		}
	}

	@Override
	public void removed() {
		observers.clear();
	}
}