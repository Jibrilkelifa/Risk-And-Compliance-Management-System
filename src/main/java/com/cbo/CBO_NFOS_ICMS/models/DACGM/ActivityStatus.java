package com.cbo.CBO_NFOS_ICMS.models.DACGM;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data

@NoArgsConstructor
@Entity
@Table(name = "activities_status")

public class ActivityStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false, length = 64)
    private String name;

    public ActivityStatus(String name) {
        this.name = name;
    }
}
