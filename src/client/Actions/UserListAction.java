package client.Actions;

import org.json.JSONException;

import client.Client;
import Common.src.Action;
import Common.src.Comfy;
import Common.src.Logger;

public class UserListAction extends Action {
	private Comfy comfy = null;

	public UserListAction(Comfy comfy) {
		this.comfy = comfy;
	}

	@Override
	public void run() {
		super.run();
		try {
			Client.users = data.getJSONArray("users");
			Logger.log("User list is " + data.getJSONArray("users"));
		} catch (JSONException e) {
			e.printStackTrace();
		};
	}
}
