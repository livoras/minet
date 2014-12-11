package client.Actions;

import org.json.JSONException;

import Common.src.Action;
import Common.src.Logger;

public class MessageAction extends Action {

	@Override
	public void run() {
		super.run();
		try {
			if (data.getString("type").equals("p2p")) showP2PChat();
			else if(data.getString("type").equals("group")) showGroupChat();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showGroupChat() throws JSONException {
		Logger.log("[group:" + data.get("to") + " - " + data.get("from") + "]" + " said: " + data.get("content"));
	}

	private void showP2PChat() throws JSONException {
		Logger.log("[p2p:" + data.get("from") + "]" + " said: " + data.get("content"));
	}
}
