public class code implements Runnable {
    public void run()
    {
        for(int i=0;i<100;i++)
        {
            
            System.out.println(i);
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         
        }
    }
    
}

