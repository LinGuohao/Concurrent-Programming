package solutions;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Pipeline1 {
	public static void main(String[] args) throws Exception {
		var NO_FURTHER_INPUT1 = "";
		var NO_FURTHER_INPUT2 = -1;

		var pool = Executors.newCachedThreadPool();

		var bq1 = new ArrayBlockingQueue<String>(1024);
		var bq2 = new ArrayBlockingQueue<Integer>(1024);

		pool.submit(() -> {
			bq1.addAll(List.of("a", "bb", "ccccccc", "ddd", "eeee", NO_FURTHER_INPUT1));
		});

		pool.submit(() -> {
			try {
				while (true) {
					var txt = bq1.take();
					if (txt.equals(NO_FURTHER_INPUT1)) {
						bq2.add(NO_FURTHER_INPUT2);
						break;
					}
					bq2.add(txt.length());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		pool.submit(() -> {
			try {
				while (true) {
					var num = bq2.take();
					if (num.equals(NO_FURTHER_INPUT2))    break;
					System.out.println(num);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		pool.shutdown();
		pool.awaitTermination(10, TimeUnit.SECONDS);
	}
}
