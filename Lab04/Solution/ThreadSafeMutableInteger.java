package hu.elte.ds;

import java.util.function.IntUnaryOperator;

public class ThreadSafeMutableInteger {

    private int value;

    public ThreadSafeMutableInteger(int initialValue) {
        value = initialValue;
    }

    public ThreadSafeMutableInteger() {
    }

    public final synchronized int get() {
        return value;
    }

    public final synchronized void set(int newValue) {
        value = newValue;
    }

    public final synchronized int getAndIncrement() {
        return value++;
    }

    public final synchronized int getAndDecrement() {
        return value--;
    }

    public final synchronized int getAndAdd(int delta) {
        int v = value;
        value += delta;
        return v;
    }

    public final synchronized int incrementAndGet() {
        return ++value;
    }

    public final synchronized int decrementAndGet() {
        return --value;
    }

    public final synchronized int addAndGet(int delta) {
        return value += delta;
    }

    /** advanced */

    public final synchronized int getAndUpdate(IntUnaryOperator updateFunction) {
        int v = value;
        value = updateFunction.applyAsInt(value);
        return v;
    }

    public final synchronized int updateAndGet(IntUnaryOperator updateFunction) {
        return value = updateFunction.applyAsInt(value);
    }
}
