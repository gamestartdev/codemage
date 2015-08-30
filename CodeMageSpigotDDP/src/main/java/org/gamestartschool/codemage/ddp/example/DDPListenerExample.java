package org.gamestartschool.codemage.ddp.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import com.keysolutions.ddpclient.DDPClient;
import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;
import com.keysolutions.ddpclient.DDPListener;

/**
 * @author kenyee
 *
 * DDP client observer that handles enough messages for unit tests to work
 */
class DDPListenerExample extends DDPListener implements Observer {
//    private final static Logger LOGGER = Logger.getLogger(DDPClient.class .getName());

    public enum DDPSTATE {
        Disconnected,
        Connected,
        LoggedIn,
        Closed,
    };
    public DDPSTATE mDdpState;
    public String mResumeToken;
    public String mUserId;
    public int mErrorCode;
    public String mErrorType;
    public String mErrorReason;
    public String mErrorMsg;
    public String mErrorSource;
    public String mSessionId;
    public int mCloseCode;
    public String mCloseReason;
    public boolean mCloseFromRemote;
    public Map<String, Map<String, Object>> mCollections;
    public String mReadySubscription;
    public String mPingId;
    
    public DDPListenerExample() {
        mDdpState = DDPSTATE.Disconnected;
        mCollections = new HashMap<String, Map<String, Object>>();
    }
    
    /** 
     * Handles processing of DDP msgs
     */
    @SuppressWarnings("unchecked")
    public void update(Observable client, Object jsonObject) {
    	
        if (jsonObject instanceof Map<?, ?>) {
        	
            Map<String, Object> json = (Map<String, Object>) jsonObject;
            // handle msg types for DDP server->client msgs: https://github.com/meteor/meteor/blob/master/packages/livedata/DDP.md
            String msgtype = (String) json.get(DDPClient.DdpMessageField.MSG);
            if (msgtype == null) {
                // ignore {"server_id":"GqrKrbcSeDfTYDkzQ"} web socket msgs
                return;
            }
            if (msgtype.equals(DdpMessageType.ERROR)) {
                mErrorSource = (String) json.get(DdpMessageField.SOURCE);
                mErrorMsg = (String) json.get(DdpMessageField.ERRORMSG);
            }
            if (msgtype.equals(DdpMessageType.CONNECTED)) {
                mDdpState = DDPSTATE.Connected;
                mSessionId = (String) json.get(DdpMessageField.SESSION);
            }
            if (msgtype.equals(DdpMessageType.CLOSED)) {
                mDdpState = DDPSTATE.Closed;
                mCloseCode = Integer.parseInt(json.get(DdpMessageField.CODE).toString());
                mCloseReason = (String) json.get(DdpMessageField.REASON);
                mCloseFromRemote = (Boolean) json.get(DdpMessageField.REMOTE);
            }
            if (msgtype.equals(DdpMessageType.ADDED)) {
                String collName = (String) json.get(DdpMessageField.COLLECTION);
//                ICodeMageCollection codeMageCollection = CodeMageCollections.get(collName);
//                codeMageCollection.Added((String) json.get(DdpMessageField.ID),
//                						(Map<String, Object>) json.get(DdpMessageField.FIELDS));
                
                if (!mCollections.containsKey(collName)) {
                    // add new collection
                    System.out.println("Added collection " + collName);
                    mCollections.put(collName, new HashMap<String, Object>());
                }
                Map<String, Object> collection = mCollections.get(collName);
                String id = (String) json.get(DdpMessageField.ID);
                System.out.println("Added docid " + id + " to collection " + collName);
                collection.put(id, json.get(DdpMessageField.FIELDS));
              //dumpMap((Map<String, Object>) jsonFields.get(DdpMessageField.FIELDS));
            }
            if (msgtype.equals(DdpMessageType.REMOVED)) {
                String collName = (String) json.get(DdpMessageField.COLLECTION);
                if (mCollections.containsKey(collName)) {
                    // remove IDs from collection
                    Map<String, Object> collection = mCollections.get(collName);
                    String docId = (String) json.get(DdpMessageField.ID);
                    System.out.println("Removed doc: " + docId);
                    collection.remove(docId);
                } else {
                	System.out.println("Received invalid removed msg for collection " + collName);
                }
            }
            if (msgtype.equals(DdpMessageType.CHANGED)) {
                // handle document updates
                String collName = (String) json.get(DdpMessageField.COLLECTION);
                if (mCollections.containsKey(collName)) {
                    Map<String, Object> collection = mCollections.get(collName);
                    String docId = (String) json.get(DdpMessageField.ID);
                    Map<String, Object> doc = (Map<String, Object>) collection.get(docId);
                    if (doc != null) {
                        // take care of field updates
                        Map<String, Object> fields = (Map<String, Object>) json.get(DdpMessageField.FIELDS);
                        if (fields != null) {
                            for(Map.Entry<String, Object> field : fields.entrySet()) {
                                String fieldname = field.getKey();
                                doc.put(fieldname, field.getValue());
                            }
                        }
                        // take care of clearing fields
                        List<String> clearfields = ((List<String>) json.get(DdpMessageField.CLEARED));
                        if (clearfields != null) {
                            for (String fieldname : clearfields) {
                                if (doc.containsKey(fieldname)) {
                                    doc.remove(fieldname);
                                }
                            }
                        }
                    }
                } else {
                	System.out.println("Received invalid changed msg for collection " + collName);
                }
            }
            //TODO: handle addedBefore, movedBefore
            //dumpMap(jsonFields);
        }
        
    }

    /**
     * Helper function to dump a map
     * @param jsonFields
     */
    public void dumpMap(Map<String, Object> jsonFields) {
        for (Entry<String, Object> entry : jsonFields.entrySet())  
        {  
          System.out.printf("key: %s, value: %s (%s)\n",   
              entry.getKey(), entry.getValue(),   
              entry.getValue().getClass());  
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onResult(Map<String, Object> jsonFields) {
        //NOTE: in normal usage, you'd add a listener per command, not a global one like this
        // handle method data collection updated msg
        String methodId = (String) jsonFields.get(DdpMessageField.ID);
        if (methodId.equals("1") && jsonFields.containsKey("result")) {
            Map<String, Object> result = (Map<String, Object>) jsonFields.get(DdpMessageField.RESULT);
            // login method is always "1"
            // REVIEW: is there a better way to figure out if it's a login result?
            mResumeToken = (String) result.get("token");
            mUserId = (String) result.get("id");
            System.out.println("Resume token: " + mResumeToken + " for user " + mUserId);
            mDdpState = DDPSTATE.LoggedIn;
        }
        if (jsonFields.containsKey("error")) {
            Map<String, Object> error = (Map<String, Object>) jsonFields.get(DdpMessageField.ERROR);
            mErrorCode = (int) Math.round((Double)error.get("error"));
            mErrorMsg = (String) error.get("message");
            mErrorType = (String) error.get("errorType");
            mErrorReason = (String) error.get("reason");
        }
        //TODO: save results for method calls
    }
    
    @Override
    public void onNoSub(String id, Map<String, Object> error) {
        if (error != null) {
            mErrorCode = (int) Math.round((Double)error.get("error"));
            mErrorMsg = (String) error.get("message");
            mErrorType = (String) error.get("errorType");
            mErrorReason = (String) error.get("reason");
        } else {
            // if there's no error, it just means a subscription was unsubscribed
            mReadySubscription = null;
        }
    }

    @Override
    public void onReady(String id) {
        mReadySubscription = id;
    }
    
    @Override
    public void onPong(String id) {
        mPingId = id;
    }
}
