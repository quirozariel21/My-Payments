package com.quiroz.mypayments.factories;

import com.quiroz.mypayments.entities.PersonalFinance;
import com.quiroz.mypayments.enums.Month;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PersonalFinanceFactory {

    public static final Long ID = 1L;
    public static final int YEAR = 2023;
    public static final Month MONTH = Month.ENERO;

    public static PersonalFinance createPersonalFinance() {
        return PersonalFinance.builder()
            .id(ID)
            .year(YEAR)
            .month(MONTH)
            .build();
    }

}
