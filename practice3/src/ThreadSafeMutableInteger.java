import java.util.function.IntUnaryOperator;

public class ThreadSafeMutableInteger {
    int a;

    public ThreadSafeMutableInteger(int a) {
        this.a = a;
    }

    public synchronized int get() {
        return this.a;
    }

    public synchronized void set(int a) {
        this.a = a;
    }

    public synchronized int getAndIncrement() {
        int tmp = a;
        this.a ++;
        return tmp;
    }

    public synchronized int getAndDecrement(){
        int tmp = this.a;
        this.a --;
        return tmp;
    }

    public synchronized int getAndAdd(int v)
    {
        int tmp = a;
        this.a = this.a +v;
        return tmp;
    }
    public synchronized int incrementAndGet(){
        this.a ++ ;
        return this.a;
    }
    public synchronized int decrementAndGet(){
        this.a --;
        return this.a ;
    }
    public synchronized int addAndGet(int v){
        this.a = this.a + v;
        return this.a;
    }

    public synchronized int getAndUpdate(IntUnaryOperator operator){
         int tmp = this.a;
         this.a = operator.applyAsInt(this.a);
         return  tmp;
    }


}
