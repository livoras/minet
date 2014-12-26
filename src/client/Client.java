package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static JSONArray users = new JSONArray();
    public static JSONArray allRooms = new JSONArray();
    public static JSONArray myRooms = new JSONArray();

    public static ArrayList<String> currentRecords;
    public static Map<String, ArrayList<String>> userChatRecords = new HashMap<String, ArrayList<String>>();
    public static Map<String, ArrayList<String>> roomChatRecords = new HashMap<String, ArrayList<String>>();

    public static int USER = 1;
    public static int GROUP = 2;
    public static int NONE = 0;
    public static int currentChatType = NONE;
    public static String currentTargetName = "";

	public static void init () throws IOException, JSONException {
        initConnection();
        initComfy();
        initActions();
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

	public static void login(String name) throws IOException, JSONException {
	    username = name;
		JSONObject data = new JSONObject();
		data.put("name", name);
		comfy.send("login", data);
	}

	public static void waitForKeyboardCommand() throws IOException, JSONException {
		while(true) {
			String str = keyboardInput.readLine();
			String[] args = str.split(":");
			String op = args[0];
			switch(op) {
				case "fuck":
					break;
				case "p":
//					p2pChat(args);
					break;
				case "g":
//					groupChat(args);
					break;
				case "cg":
					createGroupChat(args);
					break;
				case "eg":
//					enterGroupChat(args);
					break;
//				default: prompt();
			}
		}
	}

	public static void enterGroupChat(String roomName) throws JSONException {
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

	public static void groupChat(String content) throws JSONException {
		String roomName = currentTargetName;
		if(!amIInTheRoom(roomName)) {
			Logger.log("You are not in the room " + roomName + ", you can enter first!");
		} else {
			Sender.sendGroupChat(roomName, content);
		}
	}

    public static void p2pChat(String content) throws JSONException {
		String toWhom = currentTargetName;
		if (!isUserExist(toWhom)) {
			Logger.log("User " + toWhom + " is not available.");
		} else {
		    createMyRecord(content);
			Sender.sendP2PChat(toWhom, content);
		}
	}

	private static void createMyRecord(String content) {
	    currentRecords.add(username + " : " + content);
	    UI.recordModel.update();
    }

    public static ArrayList<String> updateCurrentRecordsCreateRecordsIfNotExist() {
        Map<String, ArrayList<String>> recordsMap = null;
	    ArrayList<String> records = null;
        recordsMap = currentChatType == USER ? userChatRecords: roomChatRecords;
        records = recordsMap.get(currentTargetName);
        if (records == null) {
            records = new ArrayList<String>();
            recordsMap.put(currentTargetName, records);
        }
        currentRecords = records;
        return records;
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
