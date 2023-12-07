package com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "modules")
@Data

@NoArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    private String code;
    private String name;
    private String url;
    private Boolean status;

    public Module(String name, String url, Boolean status) {
        this.name = name;
        this.url = url;
        this.status = status;
    }
}
