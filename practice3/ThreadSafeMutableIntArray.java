public class ThreadSafeMutableIntArray {
    private int[] array;
    private Object[]lockaray ;
    public ThreadSafeMutableIntArray(int capacity){
        array = new int[capacity];
        lockaray = new Object[capacity];
    }
    public void set(int value, int pos){
        if(pos<0 || pos>array.length-1){
            throw new  IllegalArgumentException();
        }
        synchronized(this.lockaray[pos]){
            this.lockaray[pos] = value;
        }
    }
    public int get(int pos){
        if(pos<0 || pos>array.length-1){
            throw new  IllegalArgumentException();
        }
        synchronized(this.lockaray[pos]){
            return this.array[pos];
        }

    }
}
