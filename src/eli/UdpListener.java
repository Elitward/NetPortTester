package eli;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Date;



public class UdpListener extends BaseListener{
	int port = 0;
	DatagramSocket socket = null;
	
	final static int BUFFSIZE = 1024*10;
	
	UdpListener(int port){
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			//e.printStackTrace();
			System.out.println("ERROR: UDP cannot listen on port " + port);
		}
	}

	@Override
	public void loop() {
		if(socket!=null){
			while(true){
				//receive
				byte[] recvData = new byte[BUFFSIZE];
				DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);
				try {
					socket.receive(recvPacket);
					System.out.println("On UDP Port " + port + " received. [" + new String(recvPacket.getData(), 0, recvPacket.getLength()) + "]");
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}

				//send
				String serverMessage = "This is UDP Server message:" + 
						"\r\nLocal Address:  " + socket.getLocalAddress().toString() + 
						"\r\nLocal Port:     " + socket.getLocalPort() +
						"\r\nRemote Address: " + recvPacket.getAddress().toString() +
						"\r\nRemote Port:    " + recvPacket.getPort() +
						"\r\nNow Time:       " + new Timestamp( (new Date()).getTime() ) +
						"\r\n";

				byte[] sendData = serverMessage.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, recvPacket.getAddress(), recvPacket.getPort());
				try {
					socket.send(sendPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		//System.out.println("Loop finish on port " + port);
	}
}
