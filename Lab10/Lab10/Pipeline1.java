import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Pipeline1 {
    public static void main(String[] args) throws Exception {
        var NO_FURTHER_INPUT1 = "";
        var NO_FURTHER_INPUT2 = -1;

        var bq1 = // TODO create the queue
        var bq2 = // TODO create the queue

        var pool = Executors.newCachedThreadPool();

        pool.submit(() -> {
            bq1.addAll(List.of("a", "bb", "ccccccc", "ddd", "eeee", NO_FURTHER_INPUT1));
        });

        pool.submit(() -> {
            try {
                while (true) {
                    // TODO queue #1 ====> txt  len ===> queue #2
                    // TODO also handle NO_FURTHER_INPUTs
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        pool.submit(() -> {
            try {
                while (true) {
                    // TODO queue #2 ====> len ====> print it
                    // TODO also handle NO_FURTHER_INPUTs
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
    }
}
