package ru.job4j.concurrent;

public class Wget {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(
                () -> {
                    try {
                        for (int i = 0; i <= 100; i++) {
                            System.out.print("\rLoading : " + i + "%");
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        thread.start();
        thread.join();
        System.out.println("\rComplete");
    }
}

