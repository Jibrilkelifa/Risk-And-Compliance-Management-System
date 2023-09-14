package com.cbo.CBO_NFOS_ICMS.models.CIPM;

import javax.persistence.*;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.OrganizationalUnit;
import lombok.*;

@Data

@NoArgsConstructor
@Entity
@Table(name = "collateral_insurance_policies")
public class CollateralInsurancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( updatable = false)
    private Long id;
    @Column( length = 64)
    private String preparedBy;
    @Column( length = 64)
    private String preparationTimeStamp;
    @Column( length = 64)
    private String authorizedBy;
    @Column( length = 64)
    private String authorizationTimeStamp;
    @Column( length = 64)
    private String borrowerName;
    @Column(length = 64)
    private String mortgagorName;
    @Column( length = 64)
    private String loanAccount;
    @Column( length = 64)
    private String loanType;
    @ManyToOne
    @JoinColumn(name = "collateral_type_id")
    private CollateralType collateralType;
    @Column( length = 64)
    private String otherCollateralType;
    @ManyToOne
    @JoinColumn(name = "insurance_coverage_type_id")
    private InsuranceCoverageType insuranceCoverageType;
    @Column( length = 64)
    private String otherInsuranceCoverageType;
    @Column( length = 64)
    private String insuredName;
    @Column(length = 64)
    private String insuranceExpireDate;

    @ManyToOne
    @JoinColumn(name = "organizational_unit_id")
    private OrganizationalUnit organizationalUnit;
}


