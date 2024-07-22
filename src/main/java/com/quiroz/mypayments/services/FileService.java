package com.quiroz.mypayments.services;

import com.quiroz.mypayments.dto.responses.CategoryFileResponseDto;
import com.quiroz.mypayments.enums.Month;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    List<CategoryFileResponseDto> loadSettings(MultipartFile file);

    void loadMyPaymentsByMonth(int year, Month month, MultipartFile file);
}
