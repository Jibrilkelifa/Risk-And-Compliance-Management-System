package com.cbo.CBO_NFOS_ICMS.models.CIPM;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.OrganizationalUnit;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@Entity
@Table(name = "collateral_insurance_policies")
public class CollateralInsurancePolicy {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "cipm_Sequence")
    @SequenceGenerator(name = "cipm_Sequence", sequenceName = "ID_SEQ")
    @Column(updatable = false)
    private Long id;
    @Column(length = 64)
    private String preparedBy;
    @Column( length = 64)
    private String preparationTimeStamp;
    @Column(length = 64)
    private String authorizedBy;
    @Column(length = 64)
    private String authorizationTimeStamp;
    @Column(length = 64)
    @NotNull
    private String borrowerName;
    @Column(length = 64)
    @NotNull
    private String mortgagorName;
    @Column(length = 64)
    @NotNull
    private String loanAccount;
    @Column(length = 64)
    @NotNull
    private String loanType;
    @ManyToOne
    @JoinColumn(name = "collateral_type_id")
    private CollateralType collateralType;
    @Column(length = 64)
    private String otherCollateralType;
    @ManyToOne
    @JoinColumn(name = "insurance_coverage_type_id")
    private InsuranceCoverageType insuranceCoverageType;
    @Column(length = 64)
    private String otherInsuranceCoverageType;
    @Column(length = 64)
    @NotNull
    private String insuredName;
    @Column(length = 64)
    private String sumInsured;
    @Column(length = 64)
    private String collateralEstimationValue;
    @Column(length = 64)
    private String insuranceExpireDate;
    private Boolean isAuthorized = false;

    @Column(length = 64)
    private String policyNumber;
    @Column(length = 64)
    private String referenceNumber;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @NotNull
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "organizational_unit_id")
    private OrganizationalUnit organizationalUnit;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @NotNull
    private Status status;

    @ManyToOne
    @JoinColumn(name = "sub_process_id")
    @NotNull
    private SubProcess subProcess;

}


