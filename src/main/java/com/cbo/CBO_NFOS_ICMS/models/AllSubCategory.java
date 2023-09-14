package com.cbo.CBO_NFOS_ICMS.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@NoArgsConstructor
@Entity
@Table(name = "all_sub_categories")

public class AllSubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable =false , updatable = false )
    private Long id;
    @Column(nullable = false, length = 64)
    private String name;
    @ManyToOne
    @JoinColumn(name = "all_category_id")
    private AllCategory allcategory;
    public AllSubCategory(String name, AllCategory allcategory) {
        this.name = name;
        this.allcategory = allcategory;
    }
}
