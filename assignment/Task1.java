import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* Task1: slicing and merging on 1 thread, sorting slices is parralelized */
public class Task1 {


  /* Create new sorted array by merging 2 smaller sorted arrays */
  private synchronized static int[] merge(int[] arr1, int[] arr2) {
    int res[] = new int[arr1.length + arr2.length];
    int f = 0;
    int s = 0;
    int index = 0;
    while (f < arr1.length || s < arr2.length) {
      if (f >= arr1.length && s < arr2.length) {
        res[index] = arr2[s];
        index = index + 1;
        s = s + 1;
      } else if (s >= arr2.length && f < arr1.length) {
        res[index] = arr1[f];
        index = index + 1;
        f = f + 1;
      } else {
        if (arr1[f] > arr2[s]) {
          res[index] = arr2[s];
          s = s + 1;
          index = index + 1;
        } else {
          res[index] = arr1[f];
          f = f + 1;
          index = index + 1;
        }
      }
    }
    return res;
  }

  /* Creates an array of arrays by slicing a bigger array into smaller chunks */
  private static int[][] slice(int[] arr, int k) {
    int num = arr.length / k;
    int[][] res = new int[k][];
    int index = 0;
    for (int i = 0; i < k; i++) {
      List<Integer> tmp = new ArrayList<>();
      for (int j = 0; j < num; j++) {
        tmp.add(arr[index]);
        
        index = index + 1;
      }
      if (i == k - 1) {
        for (int z = index; z < arr.length; z++) {
          tmp.add(arr[index]);
          index = index + 1;
        }
      }
      res[i] = new int[tmp.size()];
      for (int j = 0; j < tmp.size(); j++) {

        res[i][j] = tmp.get(j);
      }

    }
    return res;

  }

  /* Creates a sorted version of any int array */
  public static int[] sort(int[] array) {

    /* Initialize variables */
    // TODO: check available processors and create necessary variables
    int processors = Runtime.getRuntime().availableProcessors();  
    ExecutorService pool = Executors.newFixedThreadPool(processors);
    /* Turn initial array into array of smaller arrays */
    // TODO: use 'slice()' method to cut 'array' into smaller bits
    Task1Info ti = new Task1Info();
    ti.stored =  (slice(array, processors ));
    //System.out.println(ti.stored[0]);
    /* parralelized sort on the smaller arrays */
    // TODO: use multiple threads to sort smaller arrays (Arrays.sort())
    for(int i=0;i<processors;i++)
    {
      pool.submit(new MyRunable(ti));
    }
    pool.shutdown();
    /* Merge sorted smaller arrays into a singular larger one */
    // TODO: merge into one big array using 'merge()' multiple times
    // create an empty array called 'sorted' and in a for cycle use
    // 'merge(sorted, arr2d[i])' where arr2d is an array of sorted arrays
    int  sorted[] = ti.stored[0];
    for(int i=1;i<processors;i++)
    {
     
        sorted = merge(sorted, ti.stored[i]);
      
      
      //System.out.println(Arrays.toString(sorted));
    }
    /* Return fully sorted array */
    // TODO: return the sorted array and delete all lines starting with '//'
    return sorted;
  }





  


  public static void main(String[] args) {
    //sort(new int []{1,2,3,2,1,23,100,8,12,31,23,12,12,12,24,21,321,32,123,321});
    System.out.println(Arrays.toString(sort(new int []{1,2,3,2,1,23,100,8,12,31,23,12,12,12,24,21,321,32,123,321})));
  }
}

class MyRunable implements Runnable{

  private Task1Info ti;
  MyRunable(Task1Info ti)
  {
    this.ti = ti;
  }
  @Override
  public void run() {
    int num = ti.giveNum();
    Arrays.sort(ti.stored[num]);
    
    
  }
}

