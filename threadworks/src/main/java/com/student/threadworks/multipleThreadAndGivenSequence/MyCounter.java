package com.student.threadworks.multipleThreadAndGivenSequence;

import java.util.concurrent.atomic.AtomicInteger;

public final class MyCounter {

    private final AtomicInteger atomicInteger;

    public MyCounter(int initialCount) {
        this.atomicInteger = new AtomicInteger(initialCount);
    }

    // it ensures atomicity
    // it does not ensure visibility
    public final int getAndIncrementCount() {
        return atomicInteger.getAndIncrement();
    }

}
