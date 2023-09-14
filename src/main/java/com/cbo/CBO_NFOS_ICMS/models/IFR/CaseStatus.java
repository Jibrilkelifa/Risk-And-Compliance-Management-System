package com.cbo.CBO_NFOS_ICMS.models.IFR;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data

@NoArgsConstructor
@Entity
@Table(name = "cases_status")

public class CaseStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable =false , updatable = false )
    private Long id;
    @Column(nullable = false, length = 64)
    private String name;

    public CaseStatus(String name) {
        this.name = name;
    }
}
