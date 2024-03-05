package com.cbo.CBO_NFOS_ICMS.models.IFB;

import com.cbo.CBO_NFOS_ICMS.models.AllCategory;
import com.cbo.CBO_NFOS_ICMS.models.AllSubCategory;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralType;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.Status;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.OrganizationalUnit;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@NoArgsConstructor
@Entity
@Table(name = "IFB_table")
public class IFB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Column(length = 64)
    private String ifbDate;
    @Column(length = 64)
    private String caseId;
    @Column(length = 64)
    private String accountNumber;
    @Column(length = 64)
    private String borrowerName;
    @Column(length = 64)
    private String authorizedBy;
    @Column(length = 64)
    private String authorizationTimeStamp;
    private Boolean isAuthorized = false;
    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productTypes;
    @Column(length = 64)
    private String otherProductTypes;
    @ManyToOne
    @JoinColumn(name = "all_category_id")
    private AllCategory allCategory;
    @ManyToOne
    @JoinColumn(name = "all_sub_category_id")
    private AllSubCategory allSubCategory;
    @Column( length = 64)
    private String irregularity;
    @Column( length = 64)
    private String amountInvolved;
    @Column( length = 64)
    private String responsiblePerson;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "sub_process_id")
    private SubProcess subProcess;

}


