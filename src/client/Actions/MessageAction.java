package client.Actions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONException;

import client.Client;
import client.UI;
import Common.src.Action;
import Common.src.Logger;

public class MessageAction extends Action {
    public final String ENCODE = System.getProperty("file.encoding");

	@Override
	public void run() {
		super.run();
		try {
			if (data.getString("type").equals("p2p")) showP2PChat();
			else if(data.getString("type").equals("group")) showGroupChat();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showGroupChat() throws JSONException {
		Logger.log("[group:" + data.get("to") + " - " + data.get("from") + "]" + " said: " + data.get("content"));
		String roomName = data.getString("to");
        String content = data.getString("content");
		ArrayList<String> records = Client.roomChatRecords.get(roomName);
		content = data.getString("from") + " : " + content;
		if (records == null) {
		    ArrayList<String> newRecords = new ArrayList<String>();
		    newRecords.add(content);
		    Client.roomChatRecords.put(roomName, newRecords);
		} else {
		    records.add(content);
		}
		if (Client.currentChatType == Client.GROUP) updateIfShow(roomName);
	}

    private void showP2PChat() throws JSONException {
		Logger.log("[p2p:" + data.get("from") + "]" + " said: " + data.get("content"));
		String userName = data.getString("from");
        String content = userName + " : " + data.getString("content");
        try {
            content = new String(content.getBytes(), ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		ArrayList<String> records = Client.userChatRecords.get(userName);
		if (records == null) {
		    ArrayList<String> newRecords = new ArrayList<String>();
		    newRecords.add(content);
		    Client.userChatRecords.put(userName, newRecords);
		} else {
		    records.add(content);
		}
		if (Client.currentChatType == Client.USER) updateIfShow(userName);
	}

	private void updateIfShow(String name) {
	    if (name.equals(Client.currentTargetName)) {
	        UI.updateRecordListHeight();
	    } 
    }

}
