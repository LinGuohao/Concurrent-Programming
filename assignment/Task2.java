import java.util.Arrays;

/* Task2: no slicing, no bullshit memcopy, parralelized merge */
public class Task2 {
  
  /* Create new sorted array by merging 2 smaller sorted arrays */
  private static void merge(int[] src, int[] dst, int idx1, int idx2, int end) {
    // TODO: 'src' is sorted between [idx1,idx2) and [idx2,end)
    // copy both to 'dst' in a way that [idx1,end) is sorted for 'dst'
    // Note: 'idx1' is the starting point of the 1st array
    // 'idx2' is the starting point of the 2nd array
    // 'end' is the end of the 2nd array (exclusive)
    // There are no elements between the first and second arrays
    // 'src' is the source, this is where the 2 smaller sorted arrays are
    // 'dst' is the destination, this is where you have to move data
    // Merge the 2 smaller arrays using the same methodology as in 'Task1'
    int f = idx1;
    int s = idx2;
    int index = f;
    while (f < idx2 && s < end) {
      if (f < idx2 && s == end) {
        dst[index] = src[f];
        index++;
        f++;
      } else if (f == idx2 && s < end) {
        dst[index] = src[s];
        index++;
        s++;
      } else {
        if (src[f] > src[s]) {
          dst[index] = src[s];
          s++;
          index++;
        } else {
          dst[index] = src[f];
          f++;
          index++;
        }
      }
    }

  }

  /* Recursive core, calls a sibling thread until max depth is reached */
  public static void kernel(int[] src, int[] dst, int start, int end, int depth) {

    /*
     * Single thread sort if bottom has been reached // TODO: simply sort the array
     * using 'Arrays.sort()' if depth is zero.
     * 
     * /* Otherwise split task into two recursively
     */

    if (depth == 0) {
      Thread tmp = new Thread() {
        @Override
        public void run() {
          Arrays.sort(src, start, end);
        }
      };
      tmp.start();
      try {
        tmp.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } else {
      int mid = (start + end) / 2;
      Thread tmp = new Thread() {
        @Override
        public void run() {
          kernel(src, dst, start, mid, depth - 1);
        }
      };
      Thread tmp2 = new Thread() {
        @Override
        public void run() {
          kernel(src, dst, mid, end, depth - 1);
        }
      };
      tmp.start();
      tmp2.start();
      try {
        tmp.join();
        tmp2.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (depth % 2 == 0) {
        merge(src, dst, start, mid, end);
      } else {
        merge(dst, src, start, mid, end);
      }

    }
    // TODO: summon another thread and recursively sort left and right half
    // NOTE: don't forget to make recursive call with 'depth-1'

  }

  /* Creates a sorted version of any int array */
  public static int[] sort(int[] array) {

    /* Initialize variables */
    // TODO: Create 'src' and 'dst' arrays
    int[] src = array;
    int[] dst = new int[array.length];
    /* Calculate optimal depth */
    int minSize = 1000;
    int procNum = Runtime.getRuntime().availableProcessors();
    int procDepth = (int) Math.ceil(Math.log(procNum) / Math.log(2));
    int arrDepth = (int) (Math.log(array.length / minSize) / Math.log(2));
    int optDepth = Math.max(0, Math.min(procDepth, arrDepth));

    /* Launch recursive core */
    // TODO: launch kernel, call with 'optDepth' (not 'optDepth-1')

    // TODO: return src or dst depending on the parity of the used depth
    // TODO: delete all lines starting with '//'
    kernel(src, dst, 0, src.length, optDepth);
    if (optDepth % 2 == 0)
      return src;
    else
      return dst;

  }

  public static void main(String[] args) {
    System.out.println(Arrays
        .toString(sort(new int[] { 1, 2, 3, 2, 1, 23, 100, 8, 12, 31, 23, 12, 12, 12, 24, 21, 321, 32, 123, 321 })));
  }
}
