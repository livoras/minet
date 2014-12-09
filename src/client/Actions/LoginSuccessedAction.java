package client.Actions;

import java.io.IOException;

import org.json.JSONException;

import client.Client;
import Common.src.Action;
import Common.src.Comfy;
import Common.src.Logger;

public class LoginSuccessedAction extends Action {
	private Comfy comfy;

	public LoginSuccessedAction(Comfy comfy) {
		this.comfy = comfy;
	}

	@Override
	public void run() {
		super.run();
		Logger.log("Login successed!");;
		try {
			Client.waitForKeyboardCommand();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}
}
