package com.student.threadworks.multipleThreadAndGivenSequence;

public class MyWorkBalancerTest {

    public static void main(String[] args) {
        MyWorkBalancer workBalancer = new MyWorkBalancer();
        workBalancer.distibuteAndActivate(new MyCounter(1), 7, 30);
        workBalancer.shutdown();
    }

}
