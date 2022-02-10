import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class Pipeline3 {
    static int NO_FURTHER_INPUT = Integer.MAX_VALUE;

    private static <T> List<T> nCopyList(int count, IntFunction<T> makeElem) {
        return IntStream.range(0, count).mapToObj(i -> makeElem.apply(i)).toList();
    }

    public static void main(String[] args) throws Exception {
        int bound = 100;
        int stageCount = 7;

        var queues = nCopyList(stageCount + 1, n -> /* TODO create the nth queue */);

        initQueue(bound, queues);

        int[] queuedPrimes = new int[stageCount];

        var callables = new ArrayList<Callable<List<Integer>>>();
        for (int i = 0; i < stageCount; i++) {
            var idx = i;
            callables.add(() -> {
                var nonPrimes = new ArrayList<Integer>();

                try {
                    // TODO get the first number, the prime of the callable; put it in queuedPrimes

                    var isOn = true;
                    while (isOn) {
                        // TODO prev queue ====> num ===> nonPrimes  (if filtered out)
                        //                        \=====> next queue (if not filtered out)
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return nonPrimes;
            });
        }

        var pool = Executors.newCachedThreadPool();
        var futures = pool.invokeAll(callables);
        for (int i = 0; i < stageCount; i++) {
            System.out.printf("Filtered by %d: %s%n", queuedPrimes[i], futures.get(i).get());
        }

        var remainingPrimes = new ArrayList<>();
        queues.get(stageCount).drainTo(remainingPrimes);
        System.out.printf("Remaining: %s%n", remainingPrimes);

        pool.shutdown();
    }

    private static void initQueue(int bound, ArrayBlockingQueue<Integer> queue0) {
        // TODO: 3, 5, 7, ..., NO_FURTHER_INPUT ====> queue #0
    }
}
