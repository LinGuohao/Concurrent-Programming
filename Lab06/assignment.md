## 1st Assignment (Deadlocks)

Five silent philosophers sit at a round table with bowls of spaghetti. Forks are placed between each pair of adjacent philosophers. Each philosopher must alternately think and eat. However, a philosopher can only eat spaghetti when they have both left and right forks. Each fork can be held by only one philosopher at a time and so a philosopher can use the fork only if it is not being used by another philosopher. After an individual philosopher finishes eating, they need to put down both forks so that the forks become available to others. A philosopher can only take the fork on their right or the one on their left as they become available and they cannot start eating before getting both forks.

A simulation of this scenario is implemented in the class `Philosophers`. Unfortunately, the simulation often hangs in a deadlock if all the philosophers take their left fork first and wait for the right one.

Fix the simulation by changing the order of the forks taken by the philosophers to always lift the one with the lower id instead of the left one.

## 2nd Assignment (Concurrency on Containers)

A producer thread produces elements from a queue and a consumer thread consumes them and prints them. Periodically, a third thread takes a snapshot of the queuue and prints it.

Unfortunately, the code in class `ProducerConsumer` works incorrectly due to race conditions when accessing the queue.

Fix the program by making all the accesses to the queue thread-safe. Also make the output consistent by synchronizing them.
