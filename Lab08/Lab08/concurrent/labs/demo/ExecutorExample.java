package concurrent.labs.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorExample {

    private static final int MAX_WAIT_MSEC = 1000;
    private static final int MAX_AWAIT_SEC = 10;
    private static final int MAX_NUMBER_OF_PATIENTS = 100;
    private static int vaccines = 0;
    private final static AtomicInteger vaccinesCreated = new AtomicInteger(0);
    private final static AtomicInteger vaccinesUsed = new AtomicInteger(0);
    private static Object vaccineLock = new Object();
    private static List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) {
        // Doing the same thing, different implementation
        useManualThreads();
        useSingleThreadExecutor(); // Since it's single threaded, it will be very slow
        useMultiThreadExecutor();
        useScheduledExecutor(); // Just printing
    }

    private static void useManualThreads(){
        System.out.println("Starting threads manually...");

        for(int i = 0; i < MAX_NUMBER_OF_PATIENTS * 2; ++i){
            Thread thread = i % 2 == 0 ? new Thread(ExecutorExample::producer) : new Thread(ExecutorExample::consumer);
            threads.add(thread);
            thread.start();
        }

        for(Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printAndResetResult();
    }

    private static void useSingleThreadExecutor(){
        System.out.println("Starting threads with single thread executors...");

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

        for(int i = 0; i < MAX_NUMBER_OF_PATIENTS * 2; ++i){
            Runnable function = i % 2 == 0 ? ExecutorExample::producer : ExecutorExample::consumer;
            singleThreadExecutor.submit(function);
        }

        shutdownExecutor(singleThreadExecutor);
        printAndResetResult();
    }

    private static void useMultiThreadExecutor(){
        System.out.println("Starting threads with single thread executors...");

        ExecutorService executor = Executors.newFixedThreadPool(MAX_NUMBER_OF_PATIENTS);

        for(int i = 0; i < MAX_NUMBER_OF_PATIENTS * 2; ++i){
            Runnable function = i % 2 == 0 ? ExecutorExample::producer : ExecutorExample::consumer;
            executor.submit(function);
        }

        shutdownExecutor(executor);
        printAndResetResult();
    }

    private static void useScheduledExecutor(){
        System.out.println("Starting threads with single thread executors...");

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> System.out.println("Scheduled Message"), 0, 2, TimeUnit.SECONDS);

        // Letting it print 6 times
        sleepForMsec(10000);

        shutdownExecutorNow(executor);
    }

    private static void printAndResetResult(){
        System.out.println("Remaining vaccines: " + vaccines);
        System.out.println("Produced vaccines: " + vaccinesCreated.get());
        System.out.println("Used vaccines: " + vaccinesUsed.get());

        vaccines = 0;
        vaccinesUsed.set(0);
        vaccinesCreated.set(0);
    }

    private static void consumer() {
        boolean gotVaccine = false;
        while (!gotVaccine) {
            synchronized (vaccineLock){
                if (vaccines > 0){
                    --vaccines;
                    vaccinesUsed.incrementAndGet();
                    gotVaccine = true;
                }
            }
        }
    }

    private static void producer() {
        sleepSomeTime();
        synchronized (vaccineLock){
            vaccines++;
        }
        vaccinesCreated.incrementAndGet();
    }

    private static void sleepSomeTime() {
        int msec = ThreadLocalRandom.current().nextInt(MAX_WAIT_MSEC);
        sleepForMsec(msec);
    }

    private static void sleepForMsec(int msec) {
        try {
            TimeUnit.MILLISECONDS.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void shutdownExecutor(ExecutorService executor){
        try {
            System.out.println("Attempting to shut executor down...");
            executor.shutdown();
            boolean finished = executor.awaitTermination(MAX_AWAIT_SEC, TimeUnit.SECONDS);
            if (!finished){
                System.out.println("Executor couldn't finish the tasks before termination");
            }
        } catch (InterruptedException e) {
            System.err.println("Await interrupted");
        } finally {
            shutdownExecutorNow(executor);
        }
    }

    private static void shutdownExecutorNow(ExecutorService executor){
        System.out.println("Shutting down executor immediately...");
        List<Runnable> remainingTasks = executor.shutdownNow();
        System.out.println(remainingTasks.size() + " tasks remained unfinished...");
    }

}
