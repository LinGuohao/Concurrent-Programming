## 1st Assignment

We have a calendar where we schedule meetings. One meeting takes 10 minutes and must not conflict with each other. 10 threads schedule 5000 meetings per thread into the calendar while another 10 threads delete 2500 meetings per thread from the calendar. There is a 21st thread which looks for and prints the next meeting every 10 milliseconds.

Since our program utilizes the plain [`java.util.HashMap`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashMap.html) to store the meetings, which is not thread-safe, it does not terminate in most cases but throws a [`ConcurrentModificationException`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ConcurrentModificationException.html). Fix the program. To achieve this, use method [`java.util.Collections.synchronizedMap()`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collections.html#synchronizedMap(java.util.Map)) which we already used earlier and also do not forget to manually synchronize the iteration.

## 2nd Assignment

Modify the previous solution where you replace method call to `java.util.Collections.synchronizedMap()` with a new data structure [`java.util.concurrent.ConcurrentHashMap`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html). Also remove the synchronized block surrounding the iteration because using this type it is not necessary anymore. Observe (by counting the printed lines) the speed difference between the two solutions.