package com.student.threadworks.multipleThreadAndGivenSequence;

import java.util.LinkedList;
import java.util.Queue;

public final class MyWorkBalancer {

    private final Queue<MyAwakableRunnable> awakableRunnableQueue;
    private final Queue<Thread> threadQueue;

    public MyWorkBalancer() {
        this.awakableRunnableQueue = new LinkedList<>();
        this.threadQueue = new LinkedList<>();
    }

    private final void distibute(MyCounter myCounter, int totalWorkerCount) {
        int workerCount = 1;
        while (workerCount <= totalWorkerCount) {
            MyAwakableRunnable myAwakableRunnable = new MyAwakableWorker("Thread" + workerCount, myCounter);
            awakableRunnableQueue.add(myAwakableRunnable);
            Thread workerThread = new Thread(myAwakableRunnable);
            workerThread.start();
            threadQueue.add(workerThread);
            workerCount++;
        }
    }

    private final void activate(int totalWorkCount) {
        int taskCount = 1;
        while (taskCount <= totalWorkCount && !awakableRunnableQueue.isEmpty()) {
            MyAwakableRunnable myAwakableRunnable = awakableRunnableQueue.remove();
            myAwakableRunnable.awake();
            awakableRunnableQueue.add(myAwakableRunnable);
            taskCount++;
        }
    }

    public void distibuteAndActivate(MyCounter myCounter, int totalWorkerCount, int totalWorkCount) {
        distibute(myCounter, totalWorkerCount);
        activate(totalWorkCount);
    }

    public void shutdown() {
        while (!threadQueue.isEmpty()) {
            threadQueue.remove().interrupt();
        }
    }


}
