package client.Actions;

import org.json.JSONException;

import client.Client;
import Common.src.Action;
import Common.src.Logger;

public class EnterGroupSuccessedAction extends Action {
	@Override
	public void run() {
		super.run();
		String roomName;
		try {
			roomName = data.getString("room name");
			Client.myRooms.put(roomName);
			Logger.log("Enter room success!" + data);
			Logger.log("You're in these room: " + Client.myRooms);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
