package org.gamestartschool.codemage.ddp;

import java.util.Collection;
import java.util.Map;
import java.util.Observer;

interface ICodeMageCollection<T extends IMongoDocument> extends Observer {
	T get(String id);
	Collection<T> getAll();
	T Added(String id, Map<String, Object> fields);
	boolean isReady();
}