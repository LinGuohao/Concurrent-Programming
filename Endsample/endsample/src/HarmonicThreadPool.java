import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HarmonicThreadPool
{
    private final int size;
    private final long base_tick;
    private final long max_time;
    private long time;
    private ExecutorService es ;
    public HarmonicThreadPool(int n, long bt, long mt)
    {   
        this.size = n ;
        this.base_tick = bt;
        this.max_time = mt;
        this.time = 2 * bt;
        es = Executors.newFixedThreadPool(size);
    }
    public void update(long nTime){
        if(nTime > max_time){
            System.out.println("Max time exceeded! Shutting down the thread pool");
            es.shutdownNow();
        }else{
            
        }
    }






}