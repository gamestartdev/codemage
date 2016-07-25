package org.gamestartschool.codemage.ddp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MongoGroup extends AMongoDocument implements IGroup {

	public MongoGroup(String id, Map<String, Object> fields) {
		super(id, fields);
	}

	@Override
	public void removed() {

	}
	
	@Override
	public String getName() {
		return getStringField("name");
	}

	@Override
	public List<String> getMemberIds() {
		return (List<String>) fields.get("groupMembers");
		//new ArrayList makes it not a fixed-length list
		//(or at least that's what StackOverflow said)
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getWrapperIds() {
		return (List<String>) fields.get("wrappers"); 
	}

}
