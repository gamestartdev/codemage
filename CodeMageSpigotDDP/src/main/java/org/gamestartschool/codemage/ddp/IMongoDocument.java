package org.gamestartschool.codemage.ddp;

import java.util.Map;

interface IMongoDocument {
	public String getId();

	public void changed(Map<String, Object> fields);
	
}
