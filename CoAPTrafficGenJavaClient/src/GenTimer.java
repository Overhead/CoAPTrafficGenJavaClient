import java.util.Timer;
import java.util.TimerTask;

public class GenTimer {
	
	private static int interval = 1;
	private int delay = 0;
	private int period = 1000;
	static Timer timer;
	
	public GenTimer(int interval, int delay, int period)
	{
		GenTimer.interval = interval;
		this.delay = delay;
		this.period = period;
	}
	
	public void Start(){
	    timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	        	System.out.println("Time: " + setInterval());
	        }
	    }, delay, period);
	}
	
	private static final int setInterval() {
	    if (interval == 0) {
	        timer.cancel();
	        System.out.println("Time out, stopping client");
	        System.exit(-1);
	    }
	    return --interval;
	}
}
