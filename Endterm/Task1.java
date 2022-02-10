import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Task1 {
    private static final int NUM_THREADS = 10;

    public static List<String> generate(final int from, final int to, final int count) {
        if (from < 0 || to < 0 || !isInRange(count, 0, to - from + 1))
            throw new IllegalArgumentException();

        List<String> generated = new ArrayList<>(count);
        // TODO Create `NUM_THREADS` threads.
        // TODO Each thread:
        // - generates a random number in the [from, to] interval
        // - converts it into kanji using `KanjiLib.convert`
        // - creates a string "<number>, <kanji>" using the given input and converted
        // string
        // - if `generated` has size equal to `count`, it exits immediately
        // - puts the string into `generated` if it is not already present
        ExecutorService es = Executors.newFixedThreadPool(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            es.submit(() -> {
                synchronized (generated) {
                    while (generated.size() != count) {
                        int randomNumber = ThreadLocalRandom.current().nextInt(from, to + 1);
                        String converted = KanjiLib.convert(randomNumber);
                        String result = String.valueOf(randomNumber) + ", " + converted;
                        if (generated.size() != count && !generated.contains(result)) {
                            generated.add(result);
                        }
                    }
                }

            });
        }
        // TODO Start the above threads.

        // TODO Wait for each thread to finish.
        es.shutdown();
        try {
            es.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return generated;
    }

    private static boolean isInRange(int count, int from, int to) {
        return from <= count && count <= to;
    }
}
