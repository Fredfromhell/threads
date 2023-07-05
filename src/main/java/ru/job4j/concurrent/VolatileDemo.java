package ru.job4j.concurrent;

import java.util.concurrent.TimeUnit;

public class VolatileDemo {

    private static
    int count = 0;

    public static void main(String[] args) {

        Runnable maker = () -> {
            while (count < 5) {
                System.out.println("Count inc from " + count + " to " + ++count);
            }
        };

        Runnable listener = () -> {
            int temp = 0;
            while (temp < 5) {
                if (temp != count) {
                    System.out.println();
                    System.out.println("Change detected! Now count = " + count);
                    temp = count;
                }
            }
        };

       Thread one =  new Thread(listener);
       Thread two = new Thread(maker);

       two.start();
       one.start();

        System.out.println("1");
        System.out.println("2");
        System.out.println("3");

    }
}
