package ru.job4j.concurrent;

import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@AllArgsConstructor
public class Wget implements Runnable {
    private final String url;
    private final int speed;

    private final int MILLIS = 1000;

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("pom_tmp.xml")) {
            byte[] dataBuffer = new byte[speed];
            int bytesRead;
            long start = System.currentTimeMillis();
            while ((bytesRead = in.read(dataBuffer, 0, speed)) != -1) {
                long finish = System.currentTimeMillis();
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                long delta = finish - start;
                if (delta < MILLIS) {
                    Thread.sleep(MILLIS - delta);
                }
                start = System.currentTimeMillis();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        UrlValidator urlValidator = new UrlValidator();
        String url = args[0];
        if (!urlValidator.isValid(url)) {
            System.out.println("Некорректный URL в параметре 1");
        }
        try {
            int speed = Integer.parseInt(args[1]);
            Thread wget = new Thread(new Wget(url, speed));
            wget.start();
            wget.join();
        } catch (NumberFormatException e) {
            System.out.println("Введите скорость скачивания в параметр 2");
        }

    }
}

