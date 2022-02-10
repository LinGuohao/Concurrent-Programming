## Assignment 1

Let us represent a system where some clients take loans from a bank.

Create a thread pool using [Executors.newFixedThreadPool](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html#newFixedThreadPool(int)).
Use [submit()](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html#submit(java.lang.Runnable)) to add threads representing the clients into this pool.

The clients take loans in many rounds (e.g. 10000), each time using [ThreadLocalRandom.nextInt()](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ThreadLocalRandom.html#nextInt(int,int)) to determine the amount of the loan.
The bank has a (properly synchronized) counter in a variable that always shows the total amount of loans taken.
The clients themselves store how much loan they have taken, and at the end of their execution, they write this number into the appropriate element of an array.

At the end of `main`, use [shutdown()](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html#shutdown()) and [awaitTermination()](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html#awaitTermination(long,java.util.concurrent.TimeUnit)) to wait for all client threads to finish.
Once they're all done, print the bank's counter and also print the sum of the loans in the client array.
The two numbers should match.


## Assignment 2

We solve the same problem as before but now we use [a different submit() method](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html#submit(java.util.concurrent.Callable)).

At the end of the client thread's code, do not write the loan amount into an array.
Instead, let it be the return value of the anonymous function that is passed to `submit()`.
The return of `submit()` is a [Future](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Future.html), which has a [get()](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/Future.html#get()) operation that gives you the computed loan amount.

This time, invoke `shutdown()` on the pool after summing/comparing the loans in the two different ways.
