package com.cbo.CBO_NFOS_ICMS.models.IFB;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@NoArgsConstructor
@Entity
@Table(name = "ifb_statuses")

public class IfbStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)

    private Long id;
    @Column(nullable = false, length = 64)
    private String name;

    public IfbStatus(String name) {
        this.name = name;
    }
}
