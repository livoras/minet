package client;

import org.json.JSONException;
import org.json.JSONObject;

public class Sender {

	public static void sendP2PChat(String username, String content) throws JSONException {
		JSONObject toSend = new JSONObject();
		toSend.put("content", content);
		toSend.put("from", Client.username);
		toSend.put("to", username);
		Client.comfy.send("message", toSend);
	}

}
