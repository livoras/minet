package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.Actions.LoginSuccessedAction;
import client.Actions.MessageAction;
import client.Actions.RefuseLoginAction;
import client.Actions.UserListAction;
import Common.src.Comfy;
import Common.src.Configuration;
import Common.src.Logger;

public class Client {
	public static Socket socket;
	public static Comfy comfy = null;
    public static BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
    public static JSONArray users = null;
    public static String username = null;

	public static void main (String[] args) throws IOException, JSONException {
        initConnection();
        initComfy();
        initActions();
        login();
	}

	public static void login() throws IOException, JSONException {
		Logger.log("Input your username: ");
		username = keyboardInput.readLine();
		JSONObject data = new JSONObject();
		data.put("name", username);
		comfy.send("login", data);
	}

	private static void initActions() {
		comfy.accept("message", new MessageAction(comfy));
		comfy.accept("users list", new UserListAction(comfy));
		comfy.accept("login successed", new LoginSuccessedAction(comfy));
		comfy.accept("refuse login", new RefuseLoginAction(comfy));
	}

	public static void initConnection() throws IOException {
		socket = new Socket(Configuration.HOST, Configuration.PORT);
		socket.setSoTimeout(10000);
	}

	private static void initComfy() throws IOException {
		comfy = new Comfy(socket);
	}

	public static void waitForKeyboardCommand() throws IOException, JSONException {
		while(true) {
			Logger.log("==========================\n"
				+ "fuck: quit\n"
				+ "p2p: chat with someone\n"
				+ "group: group chat\n"
				+ "==========================\n");
			String str = keyboardInput.readLine();
            if (str.equals("fuck")) {
            	Logger.log("exit!");
                socket.close();
                return;
            } else if (str.equals("p2p")) {
            	p2pChat();
            } else if (str.equals("group")) {
            	groupChat();
            }
		}
	}

	public static void p2pChat() throws IOException, JSONException {
		Logger.log("Input the user's name you want to chat with: ");
		String currentUserName = null;
		while(true) {
			String str = currentUserName = keyboardInput.readLine();
			if (!userIsExit(currentUserName)) {
				Logger.log("User " + str + " does not exist!");
				continue;
			}
			break;
		}
		waitForChat(currentUserName);
	}

	private static boolean userIsExit(String currentUserName) throws JSONException {
		boolean isExist = false;
		for (int i = 0; i < users.length(); i++) {
			String name = users.getString(i);
			if (name.equals(currentUserName)) return true;
		}
		return isExist;
	}

	private static void waitForChat(String username) throws IOException, JSONException {
		Logger.log("Start to chat with " + username);
		while(true) {
			String content = keyboardInput.readLine();
			if (content.equals(":quit")) return;
			Sender.sendP2PChat(username, content);
		}
	}

	public static void groupChat() {
		Logger.log("Not yet has been implemented.");
	}

}
