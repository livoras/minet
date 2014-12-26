package client.Actions;

import org.json.JSONException;

import client.Client;
import client.UI;
import Common.src.Action;
import Common.src.Logger;

public class UserListAction extends Action {

	@Override
	public void run() {
		super.run();
		try {
			Client.users = data.getJSONArray("users");
			Logger.log("User list is " + data.getJSONArray("users"));
			UI.list_usr.setVisibleRowCount(Client.users.length());
			UI.userListModel.update();
		} catch (JSONException e) {
			e.printStackTrace();
		};
	}
}
