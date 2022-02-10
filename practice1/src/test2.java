public class test2 {
    public static void main(String[] args) throws InterruptedException {
        Thread th = new Thread(new code());
        th.start();
        th.join();
        System.out.println("======================================");
    }
}
