package com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "branches")
public class Branch {
    @Id
    private Long id;
    private String code;
    private String name;
    private String mnemonic;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private String telephone;
    @ManyToOne
    private SubProcess subProcess;

}
