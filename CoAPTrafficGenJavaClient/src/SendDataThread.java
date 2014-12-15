import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import ch.ethz.inf.vs.californium.coap.CoAP;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;
import ch.ethz.inf.vs.californium.network.CoAPEndpoint;


public class SendDataThread extends Thread {

	private TrafficConfig config;
	private int ThreadNr = 0;
	private String token;
	private static Random random = new Random();
	private int nrPackets = 0;
	private long totalRTT = 0;

	public SendDataThread(int ThreadNr, TrafficConfig config) {
		this.config = config;
		this.ThreadNr = ThreadNr;
	}

	public long getTotalRTT(){
		return totalRTT;
	}
	
	public int getNrOfPackets(){
		return nrPackets;
	}
	
	public void run() {
		try {
			CoAPEndpoint control = new CoAPEndpoint(config.toNetworkConfig());
			control.start();
			
			if(ThreadNr == 1) { //Start timer when first thread has started sending
				float time = config.getDecimalSetting(Settings.TRAFFIC_MAXSENDTIME);
				new GenTimer((int)time, 0, 1000).Start();
			}
			
			SendData(control);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SendData(CoAPEndpoint control) {
		try {		
			SendPost(control);
			//Thread.sleep(1);
			//SendGet(dataEndpoint); //Throws error on server
			//Thread.sleep(1);
			//SendDelete(dataEndpoint);
			//Thread.sleep(200);
			SendData(control);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SendPost(CoAPEndpoint dataEndpoint) {
		try {
			int payloadsize = config.getIntegerSetting(Settings.TRAFFIC_MESSAGESIZE);
			Request test = Request.newPost();
			String testURI = String.format(
					"coap://%1$s:%2$d/testing",
					config.getStringSetting(Settings.TEST_SERVER),
					config.getIntegerSetting(Settings.TEST_SERVERPORT));
			CoAP.Type type = config.getStringSetting(Settings.COAP_MESSAGETYPE).equals("CON") ? CoAP.Type.CON : CoAP.Type.NON;
			test.setURI(testURI);
			test.setType(type);
			test.setPayload(PayloadGenerator.generateRandomData(random.nextLong(), payloadsize));
			test.send(dataEndpoint);
			Response response = test.waitForResponse();
			nrPackets++;
			totalRTT = totalRTT + response.getRTT();
			token = response.getTokenString();
			
			/*File root = new File(System.getProperty("user.home"));
			File appRoot = new File(root, "trafikgeneratorcoap");
			File subDir = new File(appRoot, "logs");
			File myFile = new File(subDir, "/Test-"+ config.getIntegerSetting(Settings.TRAFFIC_NRTHREADS) + "Thread(s)-"+ThreadNr+".txt");
			myFile.createNewFile();
			
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(myFile), "utf-8"));
		    writer.append("-------------------------------------------------\n");
		    writer.append("Nr of packets sent: " + nrPackets+ "\n");
		    writer.append("Total RTT: " + totalRTT + "\n");
		    writer.append("Avg RTT: " + (totalRTT / nrPackets) + "\n");
		    writer.append("-------------------------------------------------\n");
			writer.close();*/
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void SendDelete(CoAPEndpoint dataEndpoint) {
		try {
			Request controlRequest = Request.newDelete();
			controlRequest.setURI(String.format("coap://%1$s/control?token=%2$s", config.getStringSetting(Settings.TEST_SERVER), token));
			controlRequest.send(dataEndpoint);
			Response response2 = controlRequest.waitForResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
