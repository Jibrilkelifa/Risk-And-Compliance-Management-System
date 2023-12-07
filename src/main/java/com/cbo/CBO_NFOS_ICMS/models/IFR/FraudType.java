package com.cbo.CBO_NFOS_ICMS.models.IFR;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data

@NoArgsConstructor
@Entity
@Table(name = "fraud_types")
public class FraudType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    private String name;

    public FraudType(String name) {
        this.name = name;
    }
}
