


public class Generator {

	private static int NR_THREADS = 1;
	private static String ServerIP = "";
	private static int ServerPort = 5683;
	private static int MaxMsgSize = 1400;
	private static float SimulationTime = 4.0f;
	private final static String MsgType = "CON";
	private static TrafficConfig config;
	

	public static void main(String[] args) {

		try {
			if (args.length < 5) {
	
				System.out.println("\nCoAP Trafficgenerator Client");
				System.out.println("(c) 2014, Luleå University of Technology, Sweden");
				System.out.println();
				System.out.println("Usage: " + Generator.class.getSimpleName()+ " IP PORT NR-THREADS SIM-TIME MSG-SIZE");
				System.out.println("  IP       	 : The IP of the server to where CoAP traffic will be sent");
				System.out.println("  PORT       : The PORT of the server to where CoAP traffic will be sent");
				System.out.println("  NR-THREADS : Number of threads that shall generate traffic");
				System.out.println("  SIM-TIME   : How long the simulation shall run");
				System.out.println("  MSG-SIZE   : The size of each package payload in bytes");
				System.exit(-1);
			} else {
				ServerIP = args[0].toString();
				ServerPort = Integer.parseInt(args[1].toString());
				NR_THREADS = Integer.parseInt(args[2].toString());
				SimulationTime = Float.parseFloat(args[3].toString());
				MaxMsgSize = Integer.parseInt(args[4].toString());
				
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
		} catch(Exception e) {
			System.out.println("Error in run: " + e.getMessage());
			e.printStackTrace();
		}
	}	
}
