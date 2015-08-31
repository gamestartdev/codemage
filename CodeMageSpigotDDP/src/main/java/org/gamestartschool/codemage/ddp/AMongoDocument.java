package org.gamestartschool.codemage.ddp;

import java.util.List;
import java.util.Map;

public abstract class AMongoDocument implements IMongoDocument {

	protected String id;
	protected Map<String, Object> fields;

	protected AMongoDocument(String id, Map<String, Object> fields) {
		this.id = id;
		this.fields = fields;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return id + ": " + fields;
	}
	
	public void changed(Map<String, Object> fields){
		this.fields = fields;
	}

	private Object getField(String key) {
		try{
			return fields.get(key);
		} catch (Exception e){
			System.out.println("Error getting field: " + key + " from " + this);
		}
		return null;
	}
	
	protected String getStringField(String key){
		return (String) getField(key);
	}


	protected boolean getBooleanField(String key){
		return (boolean) getField(key);
	}
	
	protected List<String> getStringListField(String key){
		return (List<String>) getField(key);
	}

}
