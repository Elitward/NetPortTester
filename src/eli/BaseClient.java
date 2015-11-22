package eli;

import java.net.InetAddress;

public abstract class BaseClient {
	InetAddress address = null;
    int port = 0;
	final static int BUFFSIZE = 1024*10;
	final static int TIMEOUT = 1000;
	
	public abstract boolean contact();
}
