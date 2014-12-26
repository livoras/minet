package client.Actions;

import java.io.IOException;

import org.json.JSONException;

import client.Client;
import client.UI;
import Common.src.Action;
import Common.src.Logger;

public class LoginSuccessedAction extends Action {

	@Override
	public void run() {
		super.run();
		Logger.log("Login successed!");;
//		try {
//			Client.waitForKeyboardCommand();
//		} catch (IOException | JSONException e) {
//			e.printStackTrace();
//		}
		UI.showMainLayout();
	}
}
