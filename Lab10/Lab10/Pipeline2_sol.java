package solutions;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class Pipeline2 {
    private static <T> List<T> nCopyList(int count, IntFunction<T> makeElem) {
    	return IntStream.range(0, count).mapToObj(i -> makeElem.apply(i)).toList();
    }

    public static void main(String[] args) throws Exception {
		List<Function<Integer, Integer>> funs = List.of(
			n -> n + 1,
			n -> 2 * n + 1,
			n -> -n
		);

		var NO_FURTHER_INPUT = Integer.MAX_VALUE;

		var pool = Executors.newCachedThreadPool();

		var data = List.of(1, 2, 3, 4, 5, 6, 100);

		var queues = nCopyList(funs.size() + 1, i -> new ArrayBlockingQueue<Integer>(1024));

		pool.submit(() -> {
			queues.get(0).addAll(data);
			queues.get(0).add(NO_FURTHER_INPUT);
		});

		for (int i = 0; i < funs.size(); i++) {
			var idx = i;
			pool.submit(() -> {
				try {
					var prevQueue = queues.get(idx);
					var nextQueue = queues.get(idx+1);

					var isOn = true;
					while (isOn) {
						var num = prevQueue.take();
						isOn = num != NO_FURTHER_INPUT;
						var nextVal = isOn ? funs.get(idx).apply(num) : NO_FURTHER_INPUT;
						nextQueue.add(nextVal);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}

		pool.submit(() -> {
			try {
				while (true) {
					var num = queues.get(funs.size()).take();
					if (num == NO_FURTHER_INPUT)   break;
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
