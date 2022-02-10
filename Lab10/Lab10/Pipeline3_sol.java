package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class Pipeline3 {
    private static <T> List<T> nCopyList(int count, IntFunction<T> makeElem) {
    	return IntStream.range(0, count).mapToObj(i -> makeElem.apply(i)).toList();
    }

    public static void main(String[] args) throws Exception {
    	int bound = 100;
    	int stageCount = 7;

		var NO_FURTHER_INPUT = Integer.MAX_VALUE;

		var pool = Executors.newCachedThreadPool();

		var queues = nCopyList(stageCount + 1, i -> new ArrayBlockingQueue<Integer>(1024));

		initQueue(bound, NO_FURTHER_INPUT, queues);

		int[] queuedPrimes = new int[stageCount];

		var callables = new ArrayList<Callable<List<Integer>>>();
		for (int i = 0; i < stageCount; i++) {
			var idx = i;
			callables.add(() -> {
				var nonPrimes = new ArrayList<Integer>();

				try {
					var prevQueue = queues.get(idx);
					var nextQueue = queues.get(idx+1);

					var prime = prevQueue.take();
					queuedPrimes[idx] = prime;

					var isOn = true;
					while (isOn) {
						var num = prevQueue.take();
						isOn = num != NO_FURTHER_INPUT;

						var isNumPrime = num % prime == 0;
						if (isNumPrime)   nonPrimes.add(num);
						else              nextQueue.add(num);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				return nonPrimes;
			});
		}

		var futures = pool.invokeAll(callables);
		for (int i = 0; i < stageCount; i++) {
			System.out.printf("Filtered by %d: %s%n", queuedPrimes[i], futures.get(i).get());
		}

		var remainingPrimes = new ArrayList<>();
		queues.get(stageCount).drainTo(remainingPrimes);
		System.out.printf("Remaining: %s%n", remainingPrimes);

		pool.shutdown();
    }

	private static void initQueue(int bound, int NO_FURTHER_INPUT, List<ArrayBlockingQueue<Integer>> queues) {
		for (int i = 3; i < bound; i += 2) {
			queues.get(0).add(i);
		}
		queues.get(0).add(NO_FURTHER_INPUT);
	}
}
