## 1st Assignment

Implement class `ThreadSafeMutableInteger` which is an abstraction on the built-in type `int` and enables thread-safe reads and writes of the stored integer. The basic operations of the class are the following:
- constructor: `ThreadSafeMutableInteger(int)`
- default constructor: `ThreadSafeMutableInteger()`
- read a value: `int get()`
- write a value: `void set(int)`

Operations to ensure atomicity:
- `int getAndIncrement()`
- `int getAndDecrement()`
- `int getAndAdd(int v)`
- `int incrementAndGet()`
- `int decrementAndGet()`
- `addAndGet(int v)`

Optionally (as an extra assignment), methods which enable execution of arbitrary operations on the data:
- `int getAndUpdate(IntUnaryOperator)`
- `int updateAndGet(IntUnaryOperator)`

Finally, test your solution with as many threads as possible, invoking all the operations.

## 2nd Assignment

Implement the data structure `ThreadSafeMutableIntArray` which represents a thread-safe array of `int` type.

It is important for the implementation to allow concurrent read and write of different elements thus we must not lock the whole array in the operations, so `synchronized` **methods** are incorrect. Instead, we must create an array of `Object` which assigns a separate lock for each single element and use them for accessing the individual elements. Basic operations:
- constructor: `ThreadSafeMutableIntArray(int capacity)`, which creates an array of
- `int` with the size of `capacity`, plus and array of `Object` for locking
- access an element of a given index: `int get(int n)` (acquires lock for the `n`th element)
- overwrite an element of a given index: `void set(int n, int)` (acquires lock for the `n`th element)

Optional operations (for atomicity):
- `int updateAndGet(int n, IntUnaryOperator)`
- `int getAndUpdate(int n, IntUnaryOperator)`

Test your implementation.
