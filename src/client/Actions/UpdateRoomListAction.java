package client.Actions;

import org.json.JSONException;

import client.Client;
import Common.src.Action;
import Common.src.Logger;

public class UpdateRoomListAction extends Action {
	@Override
	public void run() {
		super.run();
		try {
			Client.allRooms = data.getJSONArray("rooms");
			Logger.log("Rooms Updated: " + Client.allRooms);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
