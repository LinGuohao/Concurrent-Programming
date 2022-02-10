import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.annotation.processing.Generated;

public class Task3 {
    private static final int NUM_THREADS = 10;
    private static final int CHANNEL_CAPACITY = 100;
    private static final int POISON_PILL = -1;
    private static final int MAX_WAIT_SEND_NUM = 100;
    private static final int MAX_WAIT_SEND_ET = 10;
    Queue<Integer> queue = new LinkedList<>();
    private List<Integer> store = new LinkedList<>();
    int index = 0;
    int count = 0;
   // boundedChannel bc;


    // TODO Declare a thread-safe data structure for holding result named `generated`.
    // TODO Declare a data structure for holding 10 "B" threads.
    // TODO Declare thread reference for "A".
    // TODO Define a data structure that will be used as a bounded communication channel between threads
    //      the maximal capacity of the channel must be `CHANNEL_CAPACITY`.

    public List<String> get() throws InterruptedException {
        // TODO Wait for all threads to finish ("A" and all "B" threads).
        //      `InterruptedException` should be propagated (not caught).
        // TODO Return a `List` of `String` containing the data from `generated`.
        //      Note: Conversion to plain `List` is needed since `generated` should have some other (thread-safe) type.
        return null;
    }

    public List<Thread> getThreads() {
        // TODO Return the references of the 10 "B" threads.
        return null;
    }

    public void interrupt() {
        // TODO Interrupt the random generation ("A") thread.
    }

    public Task3(final int from, final int to, final int count) {
        if (from < 0 || to < 0 || !isInRange(count, 0, to - from + 1)) throw new IllegalArgumentException();
        this.count = count;
        // TODO Implement the same logic as in Task2 with the following modifications:
        class boundedChannel {
            private int size = CHANNEL_CAPACITY;
            //private int send;
            private int countted = 0;
            private int index = 0;
            public boolean stopped = false;
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
                    if(stopped == false){
                   while(queue.size()<100){
                    if(index<count  ){ 
                        queue.add(store.get(index));
                       
                       index++;
                    }else
                    {
                        queue.add(POISON_PILL);
                    }
                   }
                   return queue.poll();
                }
            else
            {
                return POISON_PILL;
            }
        }
        }
    }
        // TODO Create a producer thread (A) that generates `count` random numbers on the
        //      [from, to] interval and sends them to consumers (B) using a bounded channel.
        boundedChannel bc = new boundedChannel();
        Thread A = new Thread(()->{
            while(bc.gen() ){
                
            }
            while(!Thread.currentThread().isInterrupted()){
               synchronized(queue){
                int nowsize = queue.size();
                queue.add(store.get(index));
                index++;  
                try {
                    Thread.sleep(MAX_WAIT_SEND_NUM);
                } catch (InterruptedException e) {
                   
                    e.printStackTrace();
                }
                    break;
               } 

            }
            queue.clear();
            for(int i=0 ;i<count;i++)
            {
                queue.add(POISON_PILL);
            }
        });
        
        A.start();

       
        //System.out.println(1);
        ExecutorService es = Executors.newFixedThreadPool(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            es.submit(() -> {
                while(!Thread.currentThread().isInterrupted())
                {
                    while(!queue.isEmpty()){
                        
                    }
                }


            });
        };

            



        
        
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

       





        // TODO If thread "A" is interrupted it should quit sending numbers and
        //      send `POISON_PILL` to others immediately.

        // TODO Random number generation should be implemented in the `generateNum` method.

        // TODO Sending `POISON_PILL`s should be implemented in the `sendPoisonPill` method.

        // TODO Thread "A" should wait up to `MAX_WAIT_SEND_NUM` milliseconds to send a number.
        //      If thread "A" fails to send due to timeout, it should finish immediately.

        // TODO "B" threads should also send `POISON_PILL` when interrupted and then quit.

        // TODO Start threads, but DO NOT wait for them here to finish.
    }

    private void sendPoisonPill() {
        // TODO Send `POISON_PILL` via the channel to each of the 10 "B" threads.
        // TODO After `MAX_WAIT_SEND_ET` time give up and continue to the next thread.
        synchronized(queue){
            queue.clear();
            for(int i=0;i<count;i++){
                queue.add(POISON_PILL);
            }
        }
    }

    private int generateNum(int from, int to, Set<Integer> /* Note: Suggested type, can be modified. */ sent) {
        
        synchronized(queue){
           while(queue.size()<100){
            if(index<count  ){ 
                queue.add(store.get(index));
               
               index++;
            }else
            {
                queue.add(POISON_PILL);
            }
           }
           return queue.poll();
        }
                 // TODO Return the generated unique random number.
    }

    private static boolean isInRange(int count, int from, int to) {
        return from <= count && count <= to;
    }
}
