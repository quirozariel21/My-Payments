package com.quiroz.mypayments.threads;

import com.quiroz.mypayments.enums.Month;
import com.quiroz.mypayments.services.FileService;
import org.springframework.web.multipart.MultipartFile;

public class FileThread extends Thread {

    private final FileService fileService;
    private int year;
    private Month month;
    private MultipartFile file;


    public FileThread(FileService fileService, int year,
                      Month month, MultipartFile file) {
        this.fileService = fileService;
        this.year = year;
        this.month = month;
        this.file = file;
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName() + " Start.");
        fileService.loadMyPaymentsByMonth(year, month, file);
        System.out.println("Year: " + year + " Month:" + month + " LOADED.....");
        System.out.println(Thread.currentThread().getName() + " End.");

    }
}
