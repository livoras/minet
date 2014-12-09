package client.Actions;

import java.io.IOException;

import org.json.JSONException;

import client.Client;
import Common.src.Action;
import Common.src.Comfy;
import Common.src.Logger;

public class RefuseLoginAction extends Action {
	private Comfy comfy;

	public RefuseLoginAction(Comfy comfy) {
		this.comfy = comfy;
	}

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
