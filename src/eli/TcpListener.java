package eli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

import com.sun.jmx.snmp.Timestamp;

public class TcpListener extends BaseListener {
	int port = 0;
	ServerSocket listenSocket = null;
	
	TcpListener(int port){
		this.port = port;
		try {
			listenSocket = new ServerSocket(port);
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("ERROR: TCP cannot listen on port " + port);
		}
	}

	@Override
	public void loop() {
		if(listenSocket!=null){
			while(true){
				try {
					Socket s = listenSocket.accept();
					System.out.println("Accept on port " + s.getLocalPort());
					BufferedReader inFromClient	= new BufferedReader(new InputStreamReader(s.getInputStream()));
					BufferedWriter outToClient	= new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					String serverMessage = "This is TCP Server message:" + 
							"\r\nLocal Address:  " + s.getLocalAddress().toString() + 
							"\r\nLocal Port:     " + s.getLocalPort() +
							"\r\nRemote Address: " + s.getRemoteSocketAddress().toString() +
							"\r\nRemote Port:    " + s.getPort() +
							"\r\nNow Time:       " + new Timestamp( (new Date()).getTime() ) +
							"\r\n";
					outToClient.write(serverMessage);
					outToClient.flush();

					String clientMessage = null;
					try {
						clientMessage = inFromClient.readLine();
					} catch (SocketException e) {
					}
		            
		            s.close();
		            
		            System.out.println("On TCP Port " + s.getLocalPort() + " closed. [" + clientMessage + "]");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("Loop finish on port " + port);
	}
}
