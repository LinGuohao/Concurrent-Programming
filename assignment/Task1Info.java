public class Task1Info {
    int num = -1;
    public  int stored [][];
    public int sorted [][];
    public synchronized int giveNum()
    {
       this.num ++;
       return this.num;
       
    }
    public void stored(int [][] array)
    {
      this.stored = array;
    }
}
