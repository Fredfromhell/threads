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
    private final String fileName;

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
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
                    }
                    start = System.currentTimeMillis();
                    downloadData = 0;
                }
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            throw new Exception("Корректно задайте параметры приложения");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String filename = args[2];
            Thread wget = new Thread(new Wget(url, speed, filename));
            wget.start();
            wget.join();
    }
}

