package com.quiroz.mypayments.entities;

import com.quiroz.mypayments.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tx_expense")
@EntityListeners(AuditingEntityListener.class)
public class Expense {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator"
    )
    @SequenceGenerator(
            name = "sequence-generator",
            sequenceName = "tx_expense_seq", allocationSize = 1
    )
    private Long id;
    private String note;
    @DecimalMin("0.01")
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Column(name = "expensed_date", nullable = false)
    private LocalDate expensedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_finance_id", nullable = false)
    private PersonalFinance personalFinance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Category subCategory;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
