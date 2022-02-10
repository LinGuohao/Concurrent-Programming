import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class test2{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService test =  Executors.newFixedThreadPool(1);
        test.submit(()->
        {
       
            Thread t1 = new Thread(()->{
                for(int i=0;i<1000;i++)
                {
                    System.out.println(i);
                }
                System.out.println("finish t1");
            });
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finish t");
       
        });
        System.out.println(test.isTerminated());
        test.shutdown();
        System.out.println("finish main");
        Thread.sleep(1000);
        System.out.println(test.isTerminated());
    }
    
    
}