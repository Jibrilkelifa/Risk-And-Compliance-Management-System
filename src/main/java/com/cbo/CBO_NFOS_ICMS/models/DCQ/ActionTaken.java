package com.cbo.CBO_NFOS_ICMS.models.DCQ;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@NoArgsConstructor
@Entity
@Table(name = "actions_taken")
public class ActionTaken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    private String name;

    public ActionTaken(String name) {
        this.name = name;
    }
}
