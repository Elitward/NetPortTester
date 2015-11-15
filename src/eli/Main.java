package eli;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// Parameter Examples:
		// NetPortTester UDP 10 90  -> Test port from 10 to 99 via UDP
		// NetPortTester TCP 50 70  -> Test port from 50 to 70 via TCP
		
		boolean bTcp;
		int port1, port2;
		
		if(args.length==3){	//server mode
			if(args[0].compareToIgnoreCase("tcp")==0){
				bTcp = true;
			}else if(args[0].compareToIgnoreCase("udp")==0){
				bTcp = false;
			}else {
				System.out.println("Network type error!");
				return;
			}
			
			port1  = Integer.parseInt(args[1]);
			port2  = Integer.parseInt(args[2]);
			if(port1<=0 || port1>=65536 || port2<=0 || port2>=65536){
				System.out.println("Port number error!");
				return;
			}
			
			//Start to listen at all ports
			System.out.println("Listening " + (bTcp ? "TCP" : "UDP") + " from " + port1 + " to " + port2);
			ArrayList<Thread> listThread = new ArrayList<Thread>();
			for(int p=port1; p<=port2; p++){
				BaseListener bl;
				if(bTcp){
					bl = new TcpListener(p);

				}else{
					bl = new UdpListener(p);
				}
				
				Thread t = bl.getThread();
				bl.start();
				if(t!=null){
					listThread.add(t);
				}
			}
			
			//Wait all threads to finish (infinite)
			for (Thread t : listThread) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("The main thread is going to finish.");
		}else if(args.length==2){	//UDP client mode
			UdpClient uc = new UdpClient(args[0], args[1]);
			uc.contact();
		}else {
			System.out.println("Paramter number error!");
			return;
		}

		return;
	}

}
