package com.cbo.CBO_NFOS_ICMS.models.share;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
