package client.Actions;

import org.json.JSONException;

import Common.src.Action;
import Common.src.Comfy;
import Common.src.Logger;

public class MessageAction extends Action {
	private Comfy comfy = null;

	public MessageAction(Comfy comfy) {
		this.comfy = comfy;
	}

	@Override
	public void run() {
		super.run();
		try {
			Logger.log("[" + data.get("from") + "]" + " said: " + data.get("content"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
