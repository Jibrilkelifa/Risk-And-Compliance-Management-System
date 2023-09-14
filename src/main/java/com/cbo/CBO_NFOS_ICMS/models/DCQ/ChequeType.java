package com.cbo.CBO_NFOS_ICMS.models.DCQ;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data

@NoArgsConstructor
@Entity
@Table(name = "cheque_types")
public class ChequeType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable =false , updatable = false )
    private Long id;
    @Column(nullable = false)
    private String name;

    public ChequeType(String name) {
        this.name = name;
    }
}
