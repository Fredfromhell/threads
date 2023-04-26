package ru.job4j.concurrent;

import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@AllArgsConstructor
public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private final int millis = 1000;

    @Override
    public void run() {
        File file = new File(url);
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file.getName())) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            int downloadData = 0;
            long start = System.currentTimeMillis();
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                downloadData += bytesRead;
                if (downloadData >= speed) {
                    long finish = System.currentTimeMillis();
                    long delta = finish - start;
                    if (delta < millis) {
                        Thread.sleep(millis - delta);
                        start = System.currentTimeMillis();
                        downloadData = 0;
                    }
                }
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean urlValidator(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (URISyntaxException exception) {
            System.out.println("Указан некорректный URL");
            return false;
        } catch (MalformedURLException exception) {
            System.out.println("Указан некорректный порт");
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        if (!urlValidator(url)) {
            System.out.println("Ошибка параметра 1");
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

