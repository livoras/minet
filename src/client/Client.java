package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import Common.src.Configuration;

public class Client {
	public static Socket socket;

	public static void main (String[] args) {
		try {
			initConnection();
			System.out.println("Connection OK.");
			waitForSocketInput();
			waitForKeyboardInput();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Server has no responses.");
		}
	}

	public static void initConnection() throws IOException {
		try {
            socket = new Socket(Configuration.HOST, Configuration.PORT);
            socket.setSoTimeout(10000);
		} catch (IOException e) {
			throw e;
		}
	}

	public static void waitForSocketInput() throws IOException {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Thread socketInputThread = new Thread(new Receiver(in));
			socketInputThread.start();
	}

	public static void waitForKeyboardInput() throws IOException, JSONException {
		BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
		PrintStream out = new PrintStream(socket.getOutputStream());
		while(true) {
			try {
                String str = keyboardInput.readLine();
                System.out.println(str);
                if (str.equals("fuck")) {
                	System.out.println("Good");
                	return;
                } else {
                	JSONObject toSend = new JSONObject();
                	toSend.put("content", str);
                	out.println(toSend.toString());
                }
			} catch(IOException e) {
                System.out.println(e);
			}
		}
	}
}
