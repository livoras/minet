package client.Actions;

import java.io.IOException;

import org.json.JSONException;

import client.UI;
import Common.src.Action;
import Common.src.Logger;

public class RefuseLoginAction extends Action {

	@Override
	public void run() {
		super.run();
		Logger.log(data);
		try {
			UI.login();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		} 
	}
}
