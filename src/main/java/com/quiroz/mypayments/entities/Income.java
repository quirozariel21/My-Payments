package com.quiroz.mypayments.entities;

import com.quiroz.mypayments.enums.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tx_income")
@EntityListeners(AuditingEntityListener.class)
public class Income {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator"
    )
    @SequenceGenerator(
            name = "sequence-generator",
            sequenceName = "tx_income_seq", allocationSize = 1
    )
    private Long id;
    private String name;
    private BigDecimal amount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_finance_id", referencedColumnName = "id")
    private PersonalFinance personalFinance;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
