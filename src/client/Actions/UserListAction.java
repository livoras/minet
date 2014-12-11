package client.Actions;

import org.json.JSONException;

import client.Client;
import Common.src.Action;
import Common.src.Logger;

public class UserListAction extends Action {

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
