package com.cbo.CBO_NFOS_ICMS.models.CIPM;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@NoArgsConstructor
@Entity
@Table(name = "statuses")

public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable =false , updatable = false )
    private Long id;
    @Column(nullable = false, length = 64)
    private String name;

    public Status(String name) {
        this.name = name;
    }
}
