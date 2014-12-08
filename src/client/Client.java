package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import Common.src.Action;
import Common.src.Comfy;
import Common.src.Configuration;

public class Client {
	public static Socket socket;
	public static Comfy comfy = null;

	public static void main (String[] args) {
		try {
			initConnection();
			initComfy();
			waitForMessage();
			waitForKeyboardInput();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Server has no responses.");
		}
	}

	private static void waitForMessage() {
		comfy.accept("message", new Action() {
			@Override
			public void run() {
				super.run();
				System.out.println("Data From Server: " + data);
			}
		});
	}

	public static void initConnection() throws IOException {
		try {
            socket = new Socket(Configuration.HOST, Configuration.PORT);
            socket.setSoTimeout(10000);
		} catch (IOException e) {
			throw e;
		}
	}

	private static void initComfy() throws IOException {
		comfy = new Comfy(socket);
	}

	public static void waitForKeyboardInput() throws IOException, JSONException {
		BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			try {
                String str = keyboardInput.readLine();
                if (str.equals("fuck")) {
                	System.out.println("Kill");
                	socket.close();
                	return;
                } else {
                	JSONObject toSend = new JSONObject();
                	toSend.put("content", str);
                	comfy.send("message", toSend);
                }
			} catch(IOException e) {
                System.out.println(e);
			}
		}
	}
}
