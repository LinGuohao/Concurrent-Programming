import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Task2 {
    private static final int NUM_THREADS = 10;
    private static final int CHANNEL_CAPACITY = 100;
    private static final int POISON_PILL = -1;

    public static List<String> generate(final int from, final int to, final int count) {
        if (from < 0 || to < 0 || !isInRange(count, 0, to - from + 1)) throw new IllegalArgumentException();

        List<String> generated = new ArrayList<>(count);

        // TODO Define a data structure that will be used as a bounded communication channel between threads
        //      the maximal capacity of the channel must be `CHANNEL_CAPACITY`.
    
        class boundedChannel {
            Queue<Integer> queue = new LinkedList<>();
            private List<Integer> store = new LinkedList<>();
            private int size = CHANNEL_CAPACITY;
            //private int send;
            private int countted = 0;
            private int index = 0;
            public boolean gen(){
                synchronized(store){
                    if(countted<count){
                        int added;
                        do {
                             added = ThreadLocalRandom.current().nextInt(from, to + 1);
                        } while (store.contains(added));
                        store.add(added);
                        //System.out.println(added);
                        countted ++;
                        return true;
                    }else
                    {
                        return false;
                    }
                }
            }
            public int send(){
                synchronized(queue){
                   while(queue.size()<100){
                    if(index<count){ 
                        queue.add(store.get(index));
                       
                       index++;
                    }else
                    {
                        queue.add(POISON_PILL);
                    }
                   }
                   return queue.poll();
                }
        }
        }
        
        // TODO Create a producer thread (A) that generates `count` random numbers on the
        //      [from, to] interval and sends them to consumers (B) using a bounded channel.
        boundedChannel bc = new  boundedChannel();

        Thread readomGen = new Thread(()->{
            synchronized(bc){
               while(bc.gen()){

               }
            }
        });
        
        readomGen.start();

       
        //System.out.println(1);
        ExecutorService es = Executors.newFixedThreadPool(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            es.submit(() -> {
                
                   synchronized(bc){
                       int res = bc.send();
                        while(res != POISON_PILL){
                            String converted = KanjiLib.convert(res);
                            String result = String.valueOf(res) + ", " + converted;
                            synchronized(generated){
                                generated.add(result);
                               // System.out.println(result);
                            }
                            res = bc.send();
                        }
                        }
                   });
            }


        
        
        // TODO Random numbers must be unique (use a thread-confined data structure to keep track).
        // TODO This is thread cannot be interrupted.
        // TODO When random number generation ends signal end of transmission to each other thread (B)
        //      using the `POISON_PILL` value.

        // TODO Create `NUM_THREADS` threads. Each thread:
        //      - receives a number from thread A
        //      - if the received number equals `POISON_PILL`, it exits immediately
        //      - converts the received number into kanji using `KanjiLib.convert`
        //      - creates a string "<number>, <kanji>" using the given input and converted string
        //      - puts the string into `generated` (unconditionally)

        // TODO Start the above threads (thread A and threads B, 11 overall).

        es.shutdown();
        try {
            es.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TODO Wait for each thread to finish.

        return generated;
        
    }

    private static boolean isInRange(int count, int from, int to) {
        return from <= count && count <= to;
    }

  
}
