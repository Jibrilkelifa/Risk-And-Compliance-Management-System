package com.cbo.CBO_NFOS_ICMS.models.CIPM;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@NoArgsConstructor
@Entity
@Table(name = "suspected_fraudster_professions")
public class SuspectedFraudsterProfession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false, length = 64)
    private String name;

    public SuspectedFraudsterProfession(String name) {
        this.name = name;
    }
}
