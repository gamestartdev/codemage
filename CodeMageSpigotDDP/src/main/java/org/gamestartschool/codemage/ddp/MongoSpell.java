package org.gamestartschool.codemage.ddp;

import java.util.ArrayList;
import java.util.List;

class MongoSpell implements IMongoDocument, ISpell {
	public String code;
	public String name;
	public String id;
	public String tomeId;
	private ISpellMeteorMethodCaller meteorMethods;
	private List<ISpellObserver> observers = new ArrayList<ISpellObserver>();
	private boolean status;

	public MongoSpell(String id, String tomeId, String name, String code, boolean status, ISpellMeteorMethodCaller meteorMethods) {
		this.status = status;
		this.meteorMethods = meteorMethods;
		this.id = id;
		this.tomeId = tomeId;
		this.name = name;
		this.code = code;
	}

	@Override
	public String toString() {
		return "MongoSpell [code=" + code + ", name=" + name + ", id=" + id + ", tomeId="
				+ tomeId + "]";
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setSpellMessage(String string) {
		meteorMethods.spellMessage(id, string);
	}

	@Override
	public void addObserver(ISpellObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void setStatus(boolean status) {
		meteorMethods.spellStatus(id, status);		
	}

	@Override
	public boolean getStatus() {
		return status;
	}

	public void NotifyStatusChanged(boolean status) {
		this.status = status;
		if(status){
			for (ISpellObserver observer : observers) {
				observer.requestCodeExecutionFromBrowser(this);
			}
		}
	}

}