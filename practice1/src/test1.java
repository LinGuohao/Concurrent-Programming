public class test1{
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable1 = new Runnable(){
            public void run()
            {
                for(int i=0;i<100;i++)
                {
                    System.out.println(i);
                }
            }
        };
        Thread th = new Thread(runnable);
        th.start();
        th2.start();
    }
}