package com.cbo.CBO_NFOS_ICMS.models.CIPM;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data

@NoArgsConstructor
@Entity
@Table(name = "collateral_types")
public class CollateralType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable =false , updatable = false )
    private Long id;
    @Column(nullable = false)
    private String name;

    public CollateralType(String name) {
        this.name = name;
    }
}
