

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import ch.ethz.inf.vs.californium.network.config.NetworkConfig;

public class TrafficConfig {
	private String  meta_author                     = "";
	private String  meta_title                      = "";

	private String  test_server;
	private int     test_serverport                 = 5683;
	private int     test_testport                   = 56830;
	private int     test_ntpport                    = 123;
	private int     test_repeats                    = 1;
	private float   test_intermission               = 10000.0f;
	private int     test_paralleltransfers          = 1;

	private String  coap_messagetype                = "CON";
	private int     coap_ack_timeout                = 2000;
	private float   coap_ack_random_factor          = 1.5f;
	private int     coap_max_retransmit             = 4;
	private int     coap_nstart                     = 1;

	private String  traffic_type                    = "CONSTANT_SOURCE";
	private String  traffic_mode                    = "TIME";
	private float   traffic_constant_maxsendtime    = 10.0f;
	private int     traffic_maxmessages             = 2500;
	private int 	traffic_nrthreads				= 1;
	private float   traffic_onoff_maxsendtime       = 60.0f;
	private int     traffic_rate                    = -1;
	private int     traffic_messagesize             = 100;
	private int     traffic_filesize                = 524288;
	private int     traffic_blocksize               = 512;
	private float   traffic_burst_time              = 500.0f;
	private float   traffic_idle_time               = 500.0f;
	
	static private String newline = "\n";//System.getProperty("line.separator");
	private String originalConfig;

	public TrafficConfig(){};
	
	/**
	 * @param configuration A string representation of a TrafficConfig type configuration.
	 */
	public TrafficConfig(String configuration) {
		StringBuilder trimmedOriginal = new StringBuilder();
		String[] all_rows = configuration.split(newline);
		// Set... settings.
		for (int i = 0; i < all_rows.length; i++) {
			all_rows[i] = all_rows[i].split("#", 2)[0];
			if (all_rows[i].trim().equals("")) { continue; }
			String[] row   = all_rows[i].split("\\s+", 2);
			String type    = row[0];
			String setting = row[1].split("=", 2)[0];
			String data    = row[1].split("=", 2)[1].trim();
			setting = (type + "_" + setting).toUpperCase(Locale.getDefault());
			if (setting.equals("META_TESTVERSION"))
					continue;
			switch (Settings.valueOf(setting)) {
				case TEST_INTERMISSION:            test_intermission            = Float.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case COAP_ACK_RANDOM_FACTOR:       coap_ack_random_factor       = Float.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_MAXSENDTIME:          if (traffic_type.equals("ONOFF_SOURCE"))
					                                   { traffic_onoff_maxsendtime    = Float.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue; }
					                               else
					                                   { traffic_constant_maxsendtime = Float.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue; }
				case TRAFFIC_BURST_TIME:           traffic_burst_time           = Float.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_IDLE_TIME:            traffic_idle_time            = Float.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TEST_SERVERPORT:              test_serverport              = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TEST_TESTPORT:                test_testport                = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TEST_REPEATS:                 test_repeats                 = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TEST_PARALLELTRANSFERS:       test_paralleltransfers       = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TEST_NTPPORT:                 test_ntpport                 = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case COAP_ACK_TIMEOUT:             coap_ack_timeout             = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case COAP_MAX_RETRANSMIT:          coap_max_retransmit          = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case COAP_NSTART:                  coap_nstart                  = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_MAXMESSAGES:          traffic_maxmessages          = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_RATE:                 traffic_rate                 = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_MESSAGESIZE:          traffic_messagesize          = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_NRTHREADS:			   traffic_nrthreads			= Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_FILESIZE:             traffic_filesize             = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_BLOCKSIZE:            traffic_blocksize            = Integer.valueOf(data); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case META_AUTHOR:                  meta_author                  = data; trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case META_TITLE:                   meta_title                   = data; trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TEST_SERVER:                  test_server                  = data.replaceAll("\\s+", ""); trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case COAP_MESSAGETYPE:             coap_messagetype             = data; trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_TYPE:                 traffic_type                 = data; trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				case TRAFFIC_MODE:                 traffic_mode                 = data; trimmedOriginal.append(all_rows[i].replaceFirst("\\s+", " ") + newline); continue;
				default:                           continue;
			}
		}
		originalConfig = trimmedOriginal.toString();
	}
	public Float getDecimalSetting(Settings setting) {
		switch (setting) {
			case TEST_INTERMISSION:            return test_intermission;
			case COAP_ACK_RANDOM_FACTOR:       return coap_ack_random_factor;
			case TRAFFIC_MAXSENDTIME:          if (traffic_type.equals("ONOFF_SOURCE"))
				                                   return traffic_onoff_maxsendtime;
				                               else
				                            	   return traffic_constant_maxsendtime;
			case TRAFFIC_BURST_TIME:           return traffic_burst_time;
			case TRAFFIC_IDLE_TIME:            return traffic_idle_time;
			default:                           return null;
		}
	}
	public void setDecimalSetting(Settings setting, Float value) {
		switch (setting) {
			case TEST_INTERMISSION:            test_intermission = value;
			case COAP_ACK_RANDOM_FACTOR:       coap_ack_random_factor = value;
			case TRAFFIC_MAXSENDTIME:          if (traffic_type.equals("ONOFF_SOURCE"))
				                                   traffic_onoff_maxsendtime = value;
				                               else
				                            	   traffic_constant_maxsendtime = value;
			case TRAFFIC_BURST_TIME:           traffic_burst_time = value;
			case TRAFFIC_IDLE_TIME:            traffic_idle_time = value;
			default:                           return;
		}
	}
	public Integer getIntegerSetting(Settings setting) {
		switch (setting) {
			case TEST_SERVERPORT:              return test_serverport;
			case TEST_TESTPORT:                return test_testport;
			case TEST_REPEATS:                 return test_repeats;
			case TEST_PARALLELTRANSFERS:       return test_paralleltransfers;
			case TEST_NTPPORT:                 return test_ntpport;
			case COAP_ACK_TIMEOUT:             return coap_ack_timeout;
			case COAP_MAX_RETRANSMIT:          return coap_max_retransmit;
			case COAP_NSTART:                  return coap_nstart;
			case TRAFFIC_MAXMESSAGES:          return traffic_maxmessages;
			case TRAFFIC_NRTHREADS:			   return traffic_nrthreads;
			case TRAFFIC_RATE:                 return traffic_rate;
			case TRAFFIC_MESSAGESIZE:          return traffic_messagesize;
			case TRAFFIC_FILESIZE:             return traffic_filesize;
			case TRAFFIC_BLOCKSIZE:            return traffic_blocksize;
			default:                           return null;
		}
	}
	public void setIntegerSetting(Settings setting, Integer value) {
		switch (setting) {
			case TEST_SERVERPORT:              test_serverport = value;
			case TEST_TESTPORT:                test_testport = value;
			case TEST_REPEATS:                 test_repeats = value;
			case TEST_PARALLELTRANSFERS:       test_paralleltransfers = value;
			case TEST_NTPPORT:                 test_ntpport = value;
			case COAP_ACK_TIMEOUT:             coap_ack_timeout = value;
			case COAP_MAX_RETRANSMIT:          coap_max_retransmit = value;
			case COAP_NSTART:                  coap_nstart = value;
			case TRAFFIC_MAXMESSAGES:          traffic_maxmessages = value;
			case TRAFFIC_NRTHREADS:			   traffic_nrthreads = value;
			case TRAFFIC_RATE:                 traffic_rate = value;
			case TRAFFIC_MESSAGESIZE:          traffic_messagesize = value;
			case TRAFFIC_FILESIZE:             traffic_filesize = value;
			case TRAFFIC_BLOCKSIZE:            traffic_blocksize = value;
			default:                           return;
		}
	}
	public String getStringSetting(Settings setting) {
		switch (setting) {
			case META_AUTHOR:                  return meta_author;
			case META_TITLE:                   return meta_title;
			case TEST_SERVER:                  return test_server;
			case COAP_MESSAGETYPE:             return coap_messagetype;
			case TRAFFIC_TYPE:                 return traffic_type;
			case TRAFFIC_MODE:                 return traffic_mode;
			default:                           return null;
		}
	}
	public void setStringSetting(Settings setting, String value) {
		switch (setting) {
			case META_AUTHOR:                  meta_author = value;
			case META_TITLE:                   meta_title = value;
			case TEST_SERVER:                  test_server = value;
			case COAP_MESSAGETYPE:             coap_messagetype = value;
			case TRAFFIC_TYPE:                 traffic_type = value;
			case TRAFFIC_MODE:                 traffic_mode = value;
			default:                           return;
		}
	}
	/**
	 * Create a Californium NetworkConfig object with settings detailed in the TrafficConfig object, i.e. port and congestion control parameters.
	 * @return The NetworkConfig object.
	 */
	public NetworkConfig toNetworkConfig() {
		NetworkConfig config = new NetworkConfig();
		config.setInt("DEFAULT_COAP_PORT", this.getIntegerSetting(Settings.TEST_TESTPORT));
		config.setInt("ACK_TIMEOUT", this.getIntegerSetting(Settings.COAP_ACK_TIMEOUT));
		config.setInt("NSTART", this.getIntegerSetting(Settings.COAP_NSTART));
		config.setInt("MAX_RETRANSMIT", this.getIntegerSetting(Settings.COAP_MAX_RETRANSMIT));
		config.setInt("MAX_MESSAGE_SIZE", this.getIntegerSetting(Settings.TRAFFIC_MESSAGESIZE));
		config.setFloat("ACK_RANDOM_FACTOR", this.getDecimalSetting(Settings.COAP_ACK_RANDOM_FACTOR));
		config.setInt("DEFAULT_BLOCK_SIZE", this.getIntegerSetting(Settings.TRAFFIC_BLOCKSIZE));
		return config;
	}
	static public String fileToString(String filename) {
		FileReader fil;
		StringBuilder stringBuilder;
		try {
			fil = new FileReader (filename);
			BufferedReader reader = new BufferedReader(fil);
			String line = null;
			stringBuilder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(newline);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return stringBuilder.toString();
	}
	static public String networkConfigToStringList (NetworkConfig config) {
		String list = "";
		list += "DEFAULT_COAP_PORT=" + Integer.toString(config.getInt("DEFAULT_COAP_PORT"));
		list += ",ACK_TIMEOUT=" + Integer.toString(config.getInt("ACK_TIMEOUT"));
		list += ",ACK_RANDOM_FACTOR=" + Float.toString(config.getFloat("ACK_RANDOM_FACTOR"));
		list += ",ACK_TIMEOUT_SCALE=" + Integer.toString(config.getInt("ACK_TIMEOUT_SCALE"));
		list += ",NSTART=" + Integer.toString(config.getInt("NSTART"));
		list += ",DEFAULT_LEISURE=" + Integer.toString(config.getInt("DEFAULT_LEISURE"));
		list += ",MAX_RETRANSMIT=" + Integer.toString(config.getInt("MAX_RETRANSMIT"));
		list += ",MAX_MESSAGE_SIZE=" + Integer.toString(config.getInt("MAX_MESSAGE_SIZE"));
		return list;
	}
	static public NetworkConfig stringListToNetworkConfig (String list) {
		String[] array = list.split(",");
		NetworkConfig config = new NetworkConfig();
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(""))
				continue;
			String[] setting = array[i].split("=");
			if (setting[0].equals("DEFAULT_COAP_PORT") || setting[0].equals("ACK_TIMEOUT") || setting[0].equals("ACK_TIMEOUT_SCALE") || setting[0].equals("NSTART") || setting[0].equals("DEFAULT_LEISURE") || setting[0].equals("MAX_RETRANSMIT") || setting[0].equals("MAX_MESSAGE_SIZE")) {
				config.setInt(setting[0], Integer.valueOf(setting[1]));
			}
			else if (setting[0].equals("ACK_RANDOM_FACTOR"))
				config.setFloat(setting[0], Float.valueOf(setting[1]));
		}
		return config;
	}
	static public String configToTrimmedString(String filename) {
		String config = fileToString(filename);
		String[] all_rows = config.split(newline);
		StringBuilder trimmedString = new StringBuilder(config.length());
		// Remove comments.
		for (int i = 0; i < all_rows.length; i++) {
			all_rows[i] = all_rows[i].split("#", 2)[0];
			if (all_rows[i].trim().equals("")) { continue; }
			String[] row   = all_rows[i].split("\\s+", 2);
			String type    = row[0];
			String setting = row[1].split("=", 2)[0];
			String data    = row[1].split("=", 2)[1].trim();
			setting = (type + "_" + setting).toUpperCase(Locale.getDefault());
			trimmedString.append(setting + "=" + data + newline);
		}
		return trimmedString.toString();
	}
	static public String configToString(TrafficConfig config) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Settings.META_AUTHOR.toString() + "=" + config.getStringSetting(Settings.META_AUTHOR) + newline);
		stringBuilder.append(Settings.META_TITLE.toString() + "=" + config.getStringSetting(Settings.META_TITLE) + newline);
		stringBuilder.append(Settings.TEST_SERVER.toString() + "=" + config.getStringSetting(Settings.TEST_SERVER) + newline);
		stringBuilder.append(Settings.TEST_SERVERPORT.toString() + "=" + config.getIntegerSetting(Settings.TEST_SERVERPORT) + newline);
		stringBuilder.append(Settings.TEST_TESTPORT.toString() + "=" + config.getIntegerSetting(Settings.TEST_TESTPORT) + newline);
		stringBuilder.append(Settings.TEST_NTPPORT.toString() + "=" + config.getIntegerSetting(Settings.TEST_NTPPORT) + newline);
		stringBuilder.append(Settings.TEST_REPEATS.toString() + "=" + config.getIntegerSetting(Settings.TEST_REPEATS) + newline);
		stringBuilder.append(Settings.TEST_INTERMISSION.toString() + "=" + config.getDecimalSetting(Settings.TEST_INTERMISSION) + newline);
		stringBuilder.append(Settings.TEST_PARALLELTRANSFERS.toString() + "=" + config.getIntegerSetting(Settings.TEST_PARALLELTRANSFERS) + newline);
		stringBuilder.append(Settings.COAP_MESSAGETYPE.toString() + "=" + config.getStringSetting(Settings.COAP_MESSAGETYPE) + newline);
		stringBuilder.append(Settings.COAP_ACK_TIMEOUT.toString() + "=" + config.getIntegerSetting(Settings.COAP_ACK_TIMEOUT) + newline);
		stringBuilder.append(Settings.COAP_ACK_RANDOM_FACTOR.toString() + "=" + config.getDecimalSetting(Settings.COAP_ACK_RANDOM_FACTOR) + newline);
		stringBuilder.append(Settings.COAP_MAX_RETRANSMIT.toString() + "=" + config.getIntegerSetting(Settings.COAP_MAX_RETRANSMIT) + newline);
		stringBuilder.append(Settings.COAP_NSTART.toString() + "=" + config.getIntegerSetting(Settings.COAP_NSTART) + newline);
		stringBuilder.append(Settings.TRAFFIC_TYPE.toString() + "=" + config.getStringSetting(Settings.TRAFFIC_TYPE) + newline);
		stringBuilder.append(Settings.TRAFFIC_MODE.toString() + "=" + config.getStringSetting(Settings.TRAFFIC_MODE) + newline);
		stringBuilder.append(Settings.TRAFFIC_MAXSENDTIME.toString() + "=" + config.getDecimalSetting(Settings.TRAFFIC_MAXSENDTIME) + newline);
		stringBuilder.append(Settings.TRAFFIC_MAXMESSAGES.toString() + "=" + config.getIntegerSetting(Settings.TRAFFIC_MAXMESSAGES) + newline);
		stringBuilder.append(Settings.TRAFFIC_NRTHREADS.toString() + "=" + config.getIntegerSetting(Settings.TRAFFIC_NRTHREADS) + newline);
		stringBuilder.append(Settings.TRAFFIC_RATE.toString() + "=" + config.getIntegerSetting(Settings.TRAFFIC_RATE) + newline);
		stringBuilder.append(Settings.TRAFFIC_MESSAGESIZE.toString() + "=" + config.getIntegerSetting(Settings.TRAFFIC_MESSAGESIZE) + newline);
		stringBuilder.append(Settings.TRAFFIC_FILESIZE.toString() + "=" + config.getIntegerSetting(Settings.TRAFFIC_FILESIZE) + newline);
		stringBuilder.append(Settings.TRAFFIC_BLOCKSIZE.toString() + "=" + config.getIntegerSetting(Settings.TRAFFIC_BLOCKSIZE) + newline);
		stringBuilder.append(Settings.TRAFFIC_BURST_TIME.toString() + "=" + config.getDecimalSetting(Settings.TRAFFIC_BURST_TIME) + newline);
		stringBuilder.append(Settings.TRAFFIC_IDLE_TIME.toString() + "=" + config.getDecimalSetting(Settings.TRAFFIC_IDLE_TIME) + newline);
		return stringBuilder.toString();
	}
	public String getOriginal() {
		//TODO: Should be remade so as to print out all values that have been overridden.
		return originalConfig;
	}
}
