package com.cbo.CBO_NFOS_ICMS.models.Finance;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@NoArgsConstructor
@Entity
@Table(name = "finance_statuses")

public class FinanceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)

    private Long id;
    @Column(nullable = false, length = 64)
    private String name;

    public FinanceStatus(String name) {
        this.name = name;
    }
}
