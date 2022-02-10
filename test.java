import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class test {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService test = Executors.newFixedThreadPool(1);
        test.submit(() -> {

            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
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

        System.out.println(test.isTerminated());
        while (true) {
            if (test.isTerminated()) {
                System.out.println("finish main");
                break;
            }
        }
    }

}


Thread t1 = new Thread(()->{
    for(int i=0;i<2;i++){
        Thread t2 = new Thread(()->
        {
            for(int j=0;j<100;j++)
            {

            }
        });
        t2.start();
        
    }
    t2.join();
});





import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class test{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService test =  Executors.newFixedThreadPool(1);
        test.submit(()->
        {
        
                for(int j=0;j<2;j++) {
                    doublePool.submit(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 1000; i++) {
                                System.out.println(i);
                            }
                            System.out.println("finish t1");
                        }
                    });
                }
                doublePool.shutdown();
                while(true){
                    if (doublePool.isTerminated()){
                        System.out.println("finish two threads");
                        break;
                    }
                }
            

            System.out.println("finish t");
        });
        System.out.println(test.isTerminated());
        test.shutdown();

        System.out.println(test.isTerminated());
        while(true) {
            if(test.isTerminated()) {
                System.out.println("finish main");
                break;
            }
        }
    }


}