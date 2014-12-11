package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import client.Actions.EnterGroupSuccessedAction;
import client.Actions.ErrorAction;
import client.Actions.LoginSuccessedAction;
import client.Actions.MessageAction;
import client.Actions.RefuseLoginAction;
import client.Actions.UpdateRoomListAction;
import client.Actions.UserListAction;
import Common.src.Comfy;
import Common.src.Configuration;
import Common.src.Logger;

public class Client {
	public static Socket socket;
	public static Comfy comfy = null;
    public static BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
    public static String username = null;
    public static JSONArray users = null;
    public static JSONArray allRooms = null;
    public static JSONArray myRooms = new JSONArray();

	public static void main (String[] args) throws IOException, JSONException {
        initConnection();
        initComfy();
        initActions();
        login();
	}

	public static void initConnection() throws IOException {
		socket = new Socket(Configuration.HOST, Configuration.PORT);
		socket.setSoTimeout(10000);
	}

	private static void initComfy() throws IOException {
		comfy = new Comfy(socket);
	}

	private static void initActions() {
		comfy.accept("message", new MessageAction());
		comfy.accept("error", new ErrorAction());
		comfy.accept("users list", new UserListAction());
		comfy.accept("login successed", new LoginSuccessedAction());
		comfy.accept("refuse login", new RefuseLoginAction());
		comfy.accept("enter a group successed", new EnterGroupSuccessedAction());
		comfy.accept("update room list", new UpdateRoomListAction());
	}

	public static void login() throws IOException, JSONException {
		Logger.log("Input your username: ");
		username = keyboardInput.readLine();
		JSONObject data = new JSONObject();
		data.put("name", username);
		comfy.send("login", data);
	}

	public static void waitForKeyboardCommand() throws IOException, JSONException {
		prompt();
		while(true) {
			String str = keyboardInput.readLine();
			String[] args = str.split(":");
			String op = args[0];
			switch(op) {
				case "fuck":
					break;
				case "p":
					p2pChat(args);
					break;
				case "g":
					groupChat(args);
					break;
				case "cg":
					createGroupChat(args);
					break;
				case "eg":
					enterGroupChat(args);
					break;
				default: prompt();
			}
		}
	}

	private static void enterGroupChat(String[] args) throws JSONException {
		String roomName = args[1];
		if (isRoomExist(roomName)) {
			if (amIInTheRoom(roomName)) {
				Logger.log("You've entered the room already.");
			} else {
				Sender.enterGroupChat(roomName);
			}
		} else {
			Logger.log("Room " + roomName + " you want to enter doesn't exist.");
		}
		
	}

	private static void createGroupChat(String[] args) throws JSONException {
		String newRoomName = args[1];
		Sender.createGroupChat(newRoomName);
	}

	private static void groupChat(String[] args) throws JSONException {
		String roomName, content;
		try {
			roomName = args[1];
			content = args[2];
		} catch(ArrayIndexOutOfBoundsException e) {
			Logger.log("Command is not correct, should be: `g:roomName:content`");
			e.printStackTrace();
			return;
		}
		if(amIInTheRoom(roomName)) {
			Sender.sendGroupChat(roomName, content);
		} else {
			Logger.log("You are not in the room " + roomName + ", you can enter first!");
		}
	}

	private static void p2pChat(String[] args) throws JSONException {
		String toWhom = args[1];
		String content = args[2];
		if (!isUserExist(toWhom)) {
			Logger.log("User " + toWhom + " is not available.");
		} else {
			Sender.sendP2PChat(toWhom, content);
		}
	}

	public static void prompt() {
		Logger.log("==================================================\n"
				 + ":fuck: quit - minet\n"
				 + ":p:username:content - send message to someone\n"
				 + ":g:roomname:content - send message to a room\n"
				 + ":cg:roomname - create a new room with name\n"
				 + ":eg:roomname - enter a room with name\n"
				 + "==================================================");
	}

	private static boolean isRoomExist(String roomName) throws JSONException {
		for (int i = 0; i < allRooms.length(); i++) {
			if (allRooms.getString(i).equals(roomName)) return true;
		}
		return false;
	}

	private static boolean isUserExist(String currentUserName) throws JSONException {
		for (int i = 0; i < users.length(); i++) {
			if (users.getString(i).equals(currentUserName)) return true;
		}
		return false;
	}

	private static boolean amIInTheRoom(String roomName) throws JSONException {
		for (int i = 0; i < myRooms.length(); i++) {
			if (myRooms.get(i).equals(roomName)) return true;
		}
		return false;
	}

}
