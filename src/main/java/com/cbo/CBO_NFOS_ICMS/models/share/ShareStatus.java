package com.cbo.CBO_NFOS_ICMS.models.share;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@NoArgsConstructor
@Entity
public class ShareStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false, length = 64)
    private String name;

    public ShareStatus(String name) {
        this.name = name;
    }
}
