package Binary;

import java.time.Instant;

public class LoopTest {

	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 60000000; i++) {
			System.out.println(i);
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println((end - start)/1000.0 + "초");
	}

}
