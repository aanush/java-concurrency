package com.student.threadworks.multipleThreadAndGivenSequence;

public class MyWorkBalancerTest {

    public static void main(String[] args) {
        int initialCount = 0;
        int totalWorkCount = 105;
        int totalWorkerCount = 10;
        MyWorkBalancer workBalancer = new MyWorkBalancer();
        workBalancer.distibuteAndActivate(new MyCounter(initialCount), totalWorkerCount, totalWorkCount);
        workBalancer.shutdown();
    }

}
