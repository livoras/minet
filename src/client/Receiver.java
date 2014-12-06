package client;

import java.io.BufferedReader;
import java.io.IOException;

public class Receiver implements Runnable {
	private BufferedReader in = null;

	public Receiver(BufferedReader in) {
		this.in = in;
	}

	@Override
	public void run() {
		while(true) {
			try {
				String str = in.readLine();
				if (str == null) break;
				System.out.println("Get message from server: " + str);
			} catch (IOException e) {
				;
			}
		}
	}
	
}
