package client.Actions;

import Common.src.Action;
import Common.src.Logger;

public class ErrorAction extends Action {
	@Override
	public void run() {
		super.run();
		Logger.log("Server Error: " + data);
	}
}
