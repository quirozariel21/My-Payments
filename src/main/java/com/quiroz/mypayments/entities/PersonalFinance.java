package com.quiroz.mypayments.entities;

import com.quiroz.mypayments.enums.Month;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tx_personal_finance")
@EntityListeners(AuditingEntityListener.class)
public class PersonalFinance {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator"
    )
    @SequenceGenerator(
            name = "sequence-generator",
            sequenceName = "tx_personal_finance_seq", allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Month month;

   // @OneToMany(mappedBy = "personalFinance", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "personalFinance")
    private List<Expense> expenses = new ArrayList<>();

    @EqualsAndHashCode.Exclude @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "personalFinance")
    private List<Income> incomes = new ArrayList<>();

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}
