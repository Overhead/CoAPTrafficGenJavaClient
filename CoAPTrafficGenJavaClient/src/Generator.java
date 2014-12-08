import java.util.Timer;
import java.util.TimerTask;


public class Generator {

	private final static int NR_THREADS = 1;
	private static String ServerIP = "";
	private static int ServerPort = 5683;
	private static int MaxMsgSize = 1400;
	private final static float SimulationTime = 2.0f;
	private final static String MsgType = "CON";
	private static TrafficConfig config;
	

	public static void main(String[] args) {

		if (args.length == 0) {

			System.out.println("\nCoAP Trafficgenerator Client");
			System.out.println("(c) 2014, Luleå University of Technology, Sweden");
			System.out.println();
			System.out.println("Usage: " + Generator.class.getSimpleName()+ " IP");
			System.out.println("  IP       : The IP of the server to where CoAP traffic will be sent");
			System.exit(-1);
		} else {
			ServerIP = args[0].toString();
			config = new TrafficConfig();
			config.setStringSetting(Settings.TEST_SERVER, ServerIP);
			config.setIntegerSetting(Settings.TEST_SERVERPORT, ServerPort);
			config.setDecimalSetting(Settings.TRAFFIC_MAXSENDTIME,SimulationTime);
			config.setIntegerSetting(Settings.TRAFFIC_MESSAGESIZE, MaxMsgSize);
			config.setIntegerSetting(Settings.TRAFFIC_NRTHREADS, NR_THREADS);
			config.setStringSetting(Settings.COAP_MESSAGETYPE, MsgType);

			// TODO Auto-generated method stub
			for (int i = 0; i < NR_THREADS; i++) {
				new SendDataThread(i + 1, config).start();
			}
		}
	}	
}
