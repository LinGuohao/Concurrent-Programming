package hu.elte.ds;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

public class ThreadSafeMutableIntArray {

    private final int[] elements;
    private final Object[] locks;

    public final int length;

    public ThreadSafeMutableIntArray(int capacity) {
        elements = new int[capacity];
        locks = new Object[capacity];
        length = elements.length;
        for (int i = 0; i < length; i++) {
            locks[i] = new Object();
        }
    }

    public final int get(int index) {
        Objects.checkIndex(index, length);
        synchronized (locks[index]) {
            return elements[index];
        }
    }

    public final void set(int index, int newValue) {
        Objects.checkIndex(index, length);
        synchronized (locks[index]) {
            elements[index] = newValue;
        }
    }

    /** advanced */

    public final int updateAndGet(int index, IntUnaryOperator updateFunction) {
        Objects.checkIndex(index, length);
        int v;
        synchronized (locks[index]) {
            v = elements[index];
            elements[index] = updateFunction.applyAsInt(v);
        }
        return v;
    }

    public final int getAndUpdate(int index, IntUnaryOperator updateFunction) {
        Objects.checkIndex(index, length);
        synchronized (locks[index]) {
            return elements[index] = updateFunction.applyAsInt(elements[index]);
        }
    }

}
