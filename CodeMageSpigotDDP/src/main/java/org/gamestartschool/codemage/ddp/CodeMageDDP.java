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
import com.sun.org.apache.xerces.internal.util.Status;

public class CodeMageDDP {

	private class MeteorMethodCaller implements ISpellMeteorMethodCaller, IUserMeteorMethods {

		public void serverStatus(String serverIp, String serverPort, CodeMageServerStatus status) {
			System.out.println("Sending serverStatus " + serverIp + " " + serverPort + " " + status);
			call("serverStatus", serverIp, serverPort, status);
		}

		public void spellMessage(String spellId, String message) {
			call("spellMessage", spellId, message);
		}

		public void healthUpdate(String userId, int health) {
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

	private abstract class ACodeMageCollection<T extends IMongoDocument> extends DDPListener
			implements ICodeMageCollection<T> {

		private String name;
		private boolean isReady = false;

		public ACodeMageCollection(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public boolean isReady() {
			return isReady;
		}

		private Map<String, T> documents = new HashMap<String, T>();

		public T get(String id) {
			return documents.get(id);
		}

		public Collection<T> getAll() {
			return documents.values();
		}

		abstract T documentAdded(String id, Map<String, Object> fields);

		@Override
		public void onReady(String callId) {
			super.onReady(callId);
			isReady = true;
			System.out.println(name + " Ready!!");
		}

		@SuppressWarnings("unchecked")
		@Override
		public void update(Observable client, Object jsonObject) {
			if (jsonObject instanceof Map<?, ?>) {

				Map<String, Object> json = (Map<String, Object>) jsonObject;
				String msgtype = (String) json.get(DDPClient.DdpMessageField.MSG);

				if (name.equals(json.get(DdpMessageField.COLLECTION))) {
					handleCollection(json);
				}
			}
		}

		private void handleCollection(Map<String, Object> json) {
			String msgtype = (String) json.get(DDPClient.DdpMessageField.MSG);
			String id = (String) json.get(DdpMessageField.ID);
			Map<String, Object> fields = (Map<String, Object>) json.get(DdpMessageField.FIELDS);

			if (msgtype.equals(DdpMessageType.ADDED)) {
				try {
					documents.put(id, documentAdded(id, fields));
				} catch (RuntimeException e) {
					System.out.println("ADDED notification FAILED: " + json);
				}
			} else if (msgtype.equals(DdpMessageType.CHANGED)) {
				try {
					IMongoDocument document = documents.get(id);
					document.changed(fields);
				} catch (RuntimeException e) {
					System.out.println("CHANGED notification FAILED: " + json);
				}
			} else if (msgtype.equals(DdpMessageType.REMOVED)) {
				try {
					IMongoDocument document = documents.get(id);
					document.removed();
					documents.remove(id);
				} catch (RuntimeException e) {
					System.out.println("ADDED notification FAILED: " + json);
				}
			}

			else {
				System.out.println("Unhandled DDP COLLECTION Message! " + msgtype + " " + fields.toString());

			}
		}
	}

	private DDPClient ddpClient;
	private List<ACodeMageCollection<?>> subscriptions = new ArrayList<ACodeMageCollection<?>>();

	private final ACodeMageCollection<MongoUser> users = new ACodeMageCollection<MongoUser>("users") {

		@Override
		MongoUser documentAdded(String id, Map<String, Object> fields) {
			return new MongoUser(spells, enchantments, id, fields, methodCaller);
		}
	};

	private final ACodeMageCollection<MongoSpell> spells = new ACodeMageCollection<MongoSpell>("spells") {

		@Override
		MongoSpell documentAdded(String id, Map<String, Object> fields) {
			return new MongoSpell(id, fields, methodCaller);
		}
	};

	private final ACodeMageCollection<MongoEnchantment> enchantments = new ACodeMageCollection<MongoEnchantment>(
			"enchantments") {

		@Override
		MongoEnchantment documentAdded(String id, Map<String, Object> fields) {
			return new MongoEnchantment(spells, id, fields);
		}
	};

	private boolean isReady() {
		for (ACodeMageCollection<?> collection : subscriptions) {
			if (!collection.isReady())
				return false;
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

		while (!isReady()) {
			System.out.println("Not ready..");
			Thread.sleep(1000);
		}

		reportCodeMageServerStatus(CodeMageServerStatus.ACCEPTING_PLAYERS);
	}

	public void disconnect() {
		ddpClient.disconnect();
	}

	public IUser getUser(String minecraftPlayerId) {
		for (IUser user : users.getAll()) {
			if (minecraftPlayerId.equals(user.getMinecraftUserId())) {
				return user;
			}
		}
		return NullUser.NULL;
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
		ddpClient.subscribe(collection.getName(), new Object[] {}, collection); // Passing
																				// in
																				// collection
																				// here
																				// means
																				// we
																				// get
																				// a
																				// call
																				// to
																				// "onReady"
																				// I
																				// believe?
	}

	private void doLogin(String username, String password) {
		UsernameAuth usernameAuth = new UsernameAuth(username, password);
		Object[] params = new Object[1];
		params[0] = usernameAuth;
		ddpClient.call("login", params, new DDPListener() {

			@Override
			public void onResult(Map<String, Object> resultFields) {
				super.onResult(resultFields);
				System.out.println("Logged in: " + resultFields);
			}
		});
	}
}
