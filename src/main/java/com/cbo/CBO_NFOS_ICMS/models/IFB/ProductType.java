package com.cbo.CBO_NFOS_ICMS.models.IFB;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data

@NoArgsConstructor
@Entity
@Table(name = "product_types")
public class ProductType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    private String name;

    public ProductType(String name) {
        this.name = name;
    }
}
