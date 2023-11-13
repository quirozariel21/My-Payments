package com.quiroz.mypayments.controllers;

import com.quiroz.mypayments.dto.responses.CategoryFileResponseDto;
import com.quiroz.mypayments.dto.responses.LoadFileResponseDto;
import com.quiroz.mypayments.enums.Month;
import com.quiroz.mypayments.services.FileService;
import com.quiroz.mypayments.threads.FileRunnableThread;
import com.quiroz.mypayments.threads.FileThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping("/loadSettings")
    public ResponseEntity<List<CategoryFileResponseDto>> loadSettings(@RequestParam("file") MultipartFile multipartFile) {
        return ResponseEntity.status(HttpStatus.OK).body(fileService.loadSettings(multipartFile));
    }

    @PostMapping("/loadMyPaymentsByMonth")
    public ResponseEntity<?> loadExpensesByMonth(@RequestParam("file") MultipartFile multipartFile,
                                                 @RequestParam("month") Month month,
                                                 @RequestParam("year") int year) {
        fileService.loadMyPaymentsByMonth(year, month, multipartFile);

        return ResponseEntity.status(HttpStatus.OK).body("Expenses Loaded"); //TODO
    }

    @PostMapping("/platform-threads/loadMyPaymentsByMonths")
    public ResponseEntity<?> loadExpensesByMonths(@RequestParam("file") MultipartFile multipartFile,
                                                  @RequestParam("year") int year,
                                                  @RequestParam("months")List<Month> months) throws InterruptedException {

        long start = System.currentTimeMillis();
        log.info(String.format("Loading file using platform threads, year: %s", year));
        List<FileThread> threads = new ArrayList<>();

        for (Month month : months) {
            threads.add(new FileThread(fileService, year, month, multipartFile));
        }

        for (FileThread thread : threads) {
            thread.setDaemon(Boolean.TRUE);
            thread.start();
        }

        for (FileThread thread : threads) {
            thread.join();
        }

        long end = System.currentTimeMillis();
        long executionTime = end - start;


        LoadFileResponseDto responseDto = LoadFileResponseDto.builder()
                .startTime(start)
                .endTime(end)
                .executionTime(executionTime)
                .build();
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/virtual-threads/loadMyPaymentsByMonths")
    public ResponseEntity<?> loadExpensesByMonthsVirtualThreads(@RequestParam("file") MultipartFile multipartFile,
                                                                @RequestParam("year") int year,
                                                                @RequestParam("months")List<Month> months) throws InterruptedException {

        long start = System.currentTimeMillis();
        log.info(String.format("Loading file using virtual threads, year: %s", year));
        List<Thread> threads = new ArrayList<>();

        for (Month month : months) {
            Thread virtualThread = Thread.ofVirtual().unstarted(new FileRunnableThread(fileService, year, month, multipartFile));
            threads.add(virtualThread);
        }

        for (Thread thread : threads) {
            thread.setDaemon(Boolean.TRUE);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long end = System.currentTimeMillis();
        long executionTime = end - start;

        LoadFileResponseDto responseDto = LoadFileResponseDto.builder()
                .startTime(start)
                .endTime(end)
                .executionTime(executionTime)
                .build();
        return ResponseEntity.ok().body(responseDto);
    }
}
