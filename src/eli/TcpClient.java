package eli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;


public class TcpClient extends BaseClient{
	
	public TcpClient(String serverStr, int port) {
		InetAddress address = null;
		try {
			address = InetAddress.getByName(serverStr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if(address!=null && port>0 && port<65536){
			this.address = address;
			this.port = port;
		}
	}
	
	@Override
	public boolean contact(){
		if(address!=null && port>0 && port<65536){
			
			String clientTime = ( new Timestamp( (new Date()).getTime() ) ).toString();
			String sendData = "Client Time: " + clientTime;
			
			long timeStart = new Date().getTime();

			Socket clientSocket = null;
			BufferedReader inFromServer = null;
			BufferedWriter outToServer = null;
			try {
				clientSocket = new Socket();
				clientSocket.connect(new InetSocketAddress(address, port), TIMEOUT);
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outToServer  = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
				
				outToServer.write(sendData);
				outToServer.flush();
			} catch (ConnectException e) {
				long timeEnd = new Date().getTime();
				System.out.println("TimeOut:" + (timeEnd-timeStart));
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			finally {
				if(clientSocket!=null) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(inFromServer!=null){
					try {
						inFromServer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(outToServer!=null){
					try {
						outToServer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			long timeEnd = new Date().getTime();
			
			System.out.println("Time used: " + (timeEnd-timeStart) + "ms");

			return true;
		}else{
			return false;
		}
	}

}
