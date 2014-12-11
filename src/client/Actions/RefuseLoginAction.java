package client.Actions;

import java.io.IOException;

import org.json.JSONException;

import client.Client;
import Common.src.Action;
import Common.src.Logger;

public class RefuseLoginAction extends Action {

	@Override
	public void run() {
		super.run();
		Logger.log(data);
		try {
			Client.login();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		} 
	}
}
