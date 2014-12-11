package client;

import org.json.JSONException;
import org.json.JSONObject;

public class Sender {

	public static void sendP2PChat(String username, String content) throws JSONException {
		JSONObject toSend = new JSONObject();
		toSend.put("type", "p2p");
		toSend.put("content", content);
		toSend.put("from", Client.username);
		toSend.put("to", username);
		Client.comfy.send("message", toSend);
	}

	public static void sendGroupChat(String roomName, String content) throws JSONException {
		JSONObject toSend = new JSONObject();
		toSend.put("type", "group");
		toSend.put("content", content);
		toSend.put("from", Client.username);
		toSend.put("to", roomName);
		Client.comfy.send("message", toSend);
	}

	public static void enterGroupChat(String roomName) throws JSONException {
		JSONObject toSend = new JSONObject();
		toSend.put("username", Client.username);
		toSend.put("room name", roomName);
		Client.comfy.send("enter a group", toSend);
	}

	public static void createGroupChat(String newRoomName) throws JSONException {
		JSONObject toSend = new JSONObject();
		toSend.put("room name", newRoomName);
		toSend.put("creator", Client.username);
		Client.comfy.send("create a group", toSend);
	}

}
