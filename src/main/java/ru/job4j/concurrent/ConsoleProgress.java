package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        var process = new char[]{'-', '\\', '|', '/'};
        while (!Thread.currentThread().isInterrupted()) {
            for (int i = 0; i <= 3; i++) {
                try {
                    Thread.sleep(500);
                    System.out.print("\r load: " + process[i]);
                } catch (InterruptedException e) {
                    try {
                        Thread.currentThread().join();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        }
    }

    public static void main(String[] args) {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progress.interrupt();
        System.out.print("\rЗагрукза завершена");
    }
}

