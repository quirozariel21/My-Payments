package com.quiroz.mypayments.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpensiveModel {
    private int id;
    private String category;
    private String subCategory;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private String month;
}
