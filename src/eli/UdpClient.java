package eli;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;

import com.sun.jmx.snmp.Timestamp;

public class UdpClient {
	InetAddress address = null;
    int port = 0;
	final static int BUFFSIZE = 1024*10;
	final static int TIMEOUT = 1000;
	
	public UdpClient(String serverStr, int port) {
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
	
	public boolean contact(){
		if(address!=null && port>0 && port<65536){
			
			String clientTime = ( new Timestamp( (new Date()).getTime() ) ).toString();
			byte[] sendData = ("Now Time: " + clientTime).getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);

			byte[] recvData = new byte[BUFFSIZE];
			DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);
			
			long timeStart = new Date().getTime();
			
			DatagramSocket clientSocket = null;
			try {
				clientSocket = new DatagramSocket();
				
				if(TIMEOUT>0){
					clientSocket.setSoTimeout(TIMEOUT);
				}

				//send some message to server
				clientSocket.send(sendPacket);
				
				//wait server to replay
				clientSocket.receive(recvPacket);
				
				System.out.println("Received at " + clientTime + "\n" + new String(recvPacket.getData(), 0, recvPacket.getLength()));
				
			} catch (SocketTimeoutException e) {
				long timeEnd = new Date().getTime();
				System.out.println("TimeOut:" + (timeEnd-timeStart));
				return false;
			} catch (SocketException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			finally {
				clientSocket.close();
			}
			
			long timeEnd = new Date().getTime();
			
			System.out.println("Time used:" + (timeEnd-timeStart));

			return true;
		}else{
			return false;
		}
	}
}
