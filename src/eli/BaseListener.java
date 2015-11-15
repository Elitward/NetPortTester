package eli;

public abstract class BaseListener extends Thread implements Loop{
	
	public Thread getThread(){
		return (Thread)this;
	}
	
	@Override
	public void run() {
		super.run();
		
		loop();
	}
}
