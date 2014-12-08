

import java.util.Random;

public class PayloadGenerator {
	public static byte[] generateRandomData(long seed, int size) {
		Random rnd = new Random(seed);
		byte[] dummydata = new byte[size];
		rnd.nextBytes(dummydata);
		return dummydata;
	}
}
