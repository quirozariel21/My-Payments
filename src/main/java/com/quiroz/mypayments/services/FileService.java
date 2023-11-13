package com.quiroz.mypayments.services;

import com.quiroz.mypayments.dto.responses.CategoryFileResponseDto;
import com.quiroz.mypayments.enums.Month;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface FileService {

    List<CategoryFileResponseDto> loadSettings(MultipartFile file);
    void loadMyPaymentsByMonth(int year,
                               Month month,
                               MultipartFile file);
}
