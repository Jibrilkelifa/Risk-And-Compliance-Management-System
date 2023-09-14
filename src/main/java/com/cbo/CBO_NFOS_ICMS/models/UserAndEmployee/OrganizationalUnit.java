package com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "organizational_units")
public class OrganizationalUnit {
    @Id
    private Long id;
    private String code;
    private String name;
    private String mnemonic;
    private String area;
    private String town;
    private String telephone;
    @ManyToOne
    private SubProcess subProcess;
}
