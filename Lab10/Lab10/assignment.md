## 1st Assignment

For the following exercises, you will have to fill in the blanks in `PipelineN.java` (`N`=1,2,3).

Create pipelines in the following ways.

1.  Create two [`BlockingQueue`]s: the first carries texts, the second one carries numbers.
    - Use [`ArrayBlockingQueue`] or [`LinkedBlockingQueue`] for instantiation.
    - Create three threads. The firs one submits some texts to the first queue.
    	- Alternatively, you may read the texts from a file.
    - Another thread reads the texts from the first queue and puts their lengths into the second one.
    - A third thread reads the numbers from the second queue and writes them to the standard output.
    - Put a special terminator element (`""`, `Integer.MAX_VALUE`) into the queues as the last element to indicate that no more elements are forthcoming.
    	- To better distinguish these terminators, they should have clearly marked variable names such as `NO_FURTHER_INPUT1` and `NO_FURTHER_INPUT2`.
    - Finally, stop the thread pool that handles the threads of the exercise.
1. Make a pipeline with many components.
    - We have many functions (all of them use different formulas) that look like this: `Function<Integer, Integer> fun = n -> 2 * n + 1;`
        - The type [`java.util.function.Function`] has to be imported to use it.
        - Invoke the function with an argument like this: `fun.apply(123)`
    - The pipeline has a first stage, a last stage, and several intermediate stages.
        - The first stage takes some numbers and puts them into the first [`BlockingQueue`].
        - The intermediate stages take the incoming numbers from the appropriate [`BlockingQueue`], invoke the appropriate function, and put the result of the computation into the next [`BlockingQueue`].
        - The final stage prints the elements coming out of the last queue.
1. Create a pipeline to filter primes.
    - The pipeline has `stageCount` components. [`BlockingQueue`]s connect neighbouring stages, and there is also a final [`BlockingQueue`].
    - The first [`BlockingQueue`] gets the numbers 3, 5, 7, ... up to a given upper limit, and finally `Integer.MAX_VALUE` to indicate the end of the input.
    - To represent the stages, put a total of `stageCount` `Callable`s into a thread pool that do the following.
        - Each `Callable` uses two neighbouring [`BlockingQueue`]s.
        - The stage takes the first incoming number. This will be the `prime` of the stage.
        - The stage then takes all other incoming numbers.
            - If it finds `Integer.MAX_VALUE`, it sends it on, and then the stage is done.
            - Otherwise: if `prime` divides the number, it gets filtered out (it is put into a local list). If there is a remainder, the number is possibly a prime, so it is placed into the next [`BlockingQueue`].
        - At the end of the stage, the `Callable` returns the list of filtered numbers.
    - A thread pool starts all stages using `invokeAll`. Then get their results (the filtered out numbers) and print them.
    - Also print the remaining elements, which are (almost) guaranteed to be primes.
    - The output should look like this.

    ```
    [9, 15, 21, 27, 33, 39, 45, 51, 57, 63, 69, 75, 81, 87, 93, 99]
    [25, 35, 55, 65, 85, 95]
    [49, 77, 91]
    []
    []
    []
    []
    Remaining: [23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 2147483647]
    ```

    - If the `Callable`s also store the values of their primes in an array, the printout can look even better.


    ```
    Filtered by 3: [9, 15, 21, 27, 33, 39, 45, 51, 57, 63, 69, 75, 81, 87, 93, 99]
    Filtered by 5: [25, 35, 55, 65, 85, 95]
    Filtered by 7: [49, 77, 91]
    Filtered by 11: []
    Filtered by 13: []
    Filtered by 17: []
    Filtered by 19: []
    Remaining: [23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 2147483647]
    ```

[`BlockingQueue`]: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/BlockingQueue.html
[`ArrayBlockingQueue`]: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ArrayBlockingQueue.html
[`LinkedBlockingQueue`]: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/LinkedBlockingQueue.html
[`java.util.function.Function`]: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/function/Function.html
