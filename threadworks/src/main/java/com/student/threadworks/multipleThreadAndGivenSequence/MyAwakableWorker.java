package com.student.threadworks.multipleThreadAndGivenSequence;

import org.springframework.util.Assert;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class MyAwakableWorker implements MyAwakableRunnable {

    private final String workerName;
    private final MyCounter counter;
    private final Lock lock;
    private final Condition waitingCondition;
    private final Condition waitingAfterAwakeCondition;
    private final AtomicBoolean shouldWait;
    private final AtomicBoolean shouldWaitAfterAwake;

    public MyAwakableWorker(String workerName, MyCounter counter) {
        this.workerName = workerName;
        this.counter = counter;
        this.lock = new ReentrantLock();
        this.waitingCondition = lock.newCondition();
        this.waitingAfterAwakeCondition = lock.newCondition();
        this.shouldWait = new AtomicBoolean(true);
        this.shouldWaitAfterAwake = new AtomicBoolean(true);
    }

    @Override
    public final void run() {
        lock.lock();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                shouldWait.set(true);
                while (shouldWait.get()) {
                    try {
                        waitingCondition.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                if (!Thread.currentThread().isInterrupted()) {
                    Assert.state(!shouldWait.get(), Thread.currentThread().getName() + " escaped waiting state");
                    int count = counter.getAndIncrementCount();
                    System.out.println(workerName + " prints " + count);
                    shouldWaitAfterAwake.set(false);
                    waitingAfterAwakeCondition.signalAll();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final void awake() {
        lock.lock();
        boolean interrupted = false;
        try {
            shouldWait.set(false);
            waitingCondition.signalAll();
            shouldWaitAfterAwake.set(true);
            while (shouldWaitAfterAwake.get()) {
                try {
                    waitingAfterAwakeCondition.await();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
                //throw new InterruptedException();
            }
            lock.unlock();
        }
    }

}
