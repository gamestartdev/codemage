package org.gamestartschool.codemage.ddp;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import com.keysolutions.ddpclient.DDPClient;
import com.keysolutions.ddpclient.DDPListener;
import com.keysolutions.ddpclient.UsernameAuth;
import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;

public class CodeMageDDP  {
	
	private class MeteorMethodCaller implements ISpellMeteorMethodCaller, IUserMeteorMethods {

		public void serverStatus(String serverIp, String serverPort, CodeMageServerStatus status){
			System.out.println("Sending serverStatus "+ serverIp + " "+ serverPort + " " + status);
			call("serverStatus", serverIp, serverPort, status);
		}
		
		public void spellMessage(String spellId, String message){
			call("spellMessage", spellId, message);
		}
		
		public void healthUpdate(String userId, int health){
			call("healthUpdate", userId, health);
		}
		
		
		private int call(String methodName, Object... params) {
			return ddpClient.call(methodName, params);
		}

		@Override
		public void spellSetFocus(int row, int column) {
			
		}

		@Override
		public void spellStatus(String spellId, boolean status) {
			call("spellStatus", spellId, status);
		}
	}
	private MeteorMethodCaller methodCaller = new MeteorMethodCaller();

	private abstract class ACodeMageCollection<T extends IMongoDocument> extends DDPListener implements ICodeMageCollection<T> {
		
		private String name;
		private boolean isReady = false;
		
		public String getName() {
			return name;
		}
		
		public ACodeMageCollection(String name) {
			this.name = name;
		}
		
		private Map<String, T> documents = new HashMap<String, T>();
		public T get(String id) { return documents.get(id); }
		public Collection<T> getAll() { return documents.values(); }
		
		abstract T ConstructEntity(String id, Map<String, Object> fields);
		abstract void Changed(String id, Map<String, Object> fields, T entity);
		
		public T Added(String id, Map<String, Object> fields) {
			T instance = ConstructEntity(id, fields);
			documents.put(id, instance);
			System.out.println("Adding: " + instance);
			return instance;
		}
		
		
		@Override
		public void onReady(String callId) {
			super.onReady(callId);
			isReady = true;
			System.out.println(name+" Ready!!");
		}
		
		@SuppressWarnings("unchecked")
		@Override
	    public void update(Observable client, Object jsonObject) {
	        if (jsonObject instanceof Map<?, ?>) {
	            Map<String, Object> json = (Map<String, Object>) jsonObject;
	            
	            if(name.equals(json.get(DdpMessageField.COLLECTION))) {
	                String msgtype = (String) json.get(DDPClient.DdpMessageField.MSG);
	                if (msgtype.equals(DdpMessageType.ADDED)) {
	                	try {
	                		this.Added((String) json.get(DdpMessageField.ID), (Map<String, Object>) json.get(DdpMessageField.FIELDS));
	                	} catch (RuntimeException e){
	                		System.out.println("Failed to decode DDP object: "+json);
	                	}
	                }
	                if (msgtype.equals(DdpMessageType.CHANGED)) {
	                	String id = (String) json.get(DdpMessageField.ID);
                		this.Changed(id, (Map<String, Object>) json.get(DdpMessageField.FIELDS), this.documents.get(id));
	                }
	                
	            };
	        }
	    }


		public boolean isReady() {
			return isReady;
		}
	}
	
	private DDPClient ddpClient;
	private List<ACodeMageCollection<?>> subscriptions = new ArrayList<ACodeMageCollection<?>>();

	private final ACodeMageCollection<MongoUser> users = new ACodeMageCollection<MongoUser>("users") {

		@Override
		MongoUser ConstructEntity(String id, Map<String, Object> fields) {
			String meteorUsername = (String) fields.get("username");
			String minecraftPlayerId = (String) fields.get("minecraftPlayerId");
			String role = (String) fields.get("role");
			return new MongoUser(spells, enchantments, id, meteorUsername, minecraftPlayerId, role, methodCaller);
		}

		@Override
		void Changed(String id, Map<String, Object> fields, MongoUser entity) {
		}

	};

	private final ACodeMageCollection<MongoSpell> spells = new ACodeMageCollection<MongoSpell>("spells") {

		@Override
		MongoSpell ConstructEntity(String id, Map<String, Object> fields) {
			String tomeId = (String) fields.get("tomeId");
			String name = (String) fields.get("name");
			String code = (String) fields.get("code");
			boolean status = (boolean) fields.get("status");
			return new MongoSpell(id, tomeId, name, code, status, methodCaller);
		}

		@Override
		void Changed(String id, Map<String, Object> fields, MongoSpell entity) {
			boolean status = (boolean)fields.get("status");
			System.out.println("Status Changed: " + status);
			entity.NotifyStatusChanged(status);
		}
	};

	private final ACodeMageCollection<MongoEnchantment> enchantments = new ACodeMageCollection<MongoEnchantment>(
			"enchantments") {

		@Override
		MongoEnchantment ConstructEntity(String id, Map<String, Object> fields) {
			
			String userId = (String) fields.get("userId");
			String name = (String) fields.get("name");
			String bindingString = (String) fields.get("binding");
			String bindingTrigger = (String) fields.get("trigger");
			List<String> spellIds = (List<String>)fields.get("spellIds");
			
			IEnchantmentBinding binding =  EDummyEnchantmentBinding.valueOf(bindingString.toUpperCase());
			IEnchantmentTrigger trigger =  EDummyEnchantmentTrigger.valueOf(bindingTrigger.toUpperCase());
			
			return new MongoEnchantment(spells, id, userId, name, binding, trigger, spellIds);
		}

		@Override
		void Changed(String id, Map<String, Object> fields, MongoEnchantment entity) {
		}

	};
	
	private boolean isReady() { 
		for (ACodeMageCollection<?> collection : subscriptions) {
			if(!collection.isReady())return false;
		}
		return true;
	}
	
	public CodeMageDDP(String meteorServerIp, Integer meteorServerPort) throws URISyntaxException {
		ddpClient = new DDPClient(meteorServerIp, meteorServerPort);
	}
	
	public void connect(String username, String password) throws InterruptedException {
		ddpClient.connect();
		Thread.sleep(200);

		doLogin(username, password);
		Thread.sleep(200);
		reportCodeMageServerStatus(CodeMageServerStatus.SUBSCRIBING);
		
		createSubscription(users);
		createSubscription(enchantments);
		createSubscription(spells);

		while(!isReady()){
			System.out.println("Not ready..");
			Thread.sleep(1000);
		}
		
		reportCodeMageServerStatus(CodeMageServerStatus.ACCEPTING_PLAYERS);
	}
	
	public void disconnect() {
		ddpClient.disconnect();
	}

	private void reportCodeMageServerStatus(CodeMageServerStatus status) {
		String dummyCodeMageServerIp = "127.0.0.1";
		String dummyCodeMageServerPort = "54175";
		new MeteorMethodCaller().serverStatus(dummyCodeMageServerIp, dummyCodeMageServerPort, status);
		
		System.out.println(status);
	}
	
	private void createSubscription(ACodeMageCollection<?> collection) {
		subscriptions.add(collection);
		ddpClient.addObserver(collection);
		ddpClient.subscribe(collection.getName(), new Object[] {}, collection); //Passing in collection here means we get a call to "onReady" I believe?
	}

	private void doLogin(String username, String password) {
		UsernameAuth usernameAuth = new UsernameAuth(username, password);
		Object[] params = new Object[1];
		params[0] = usernameAuth;
		ddpClient.call("login", params, new DDPListener(){
			
			@Override
			public void onResult(Map<String, Object> resultFields) {
				super.onResult(resultFields);
				System.out.println("Logged in: "+resultFields);
			}
		});
	}

	protected IUser getUser(String minecraftPlayerId) {
		for (IUser user : users.getAll()) {
			if(minecraftPlayerId.equals(user.getMinecraftUserId())){
				return user;
			}
		}
		return NullUser.NULL;
	}
}
