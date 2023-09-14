package com.cbo.CBO_NFOS_ICMS.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_modules")
public class SubModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false )
    private Long id;
    @Column(length = 10)
    private String code;
    @Column(length = 64)
    private String name;
    public SubModule(String name) {
        this.name = name;
    }
}

