package com.cbo.CBO_NFOS_ICMS.models.Finance;

import com.cbo.CBO_NFOS_ICMS.models.AllCategory;
import com.cbo.CBO_NFOS_ICMS.models.AllSubCategory;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.Status;
import com.cbo.CBO_NFOS_ICMS.models.IFB.ProductType;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "Finance_table")
public class Finance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Column(length = 64)
    private String financeDate;
    @Column(length = 64)
    private String caseId;
    @Column(length = 64)
    private String accountNumber;
    @Column(length = 64)
    private String authorizedBy;
    @Column(length = 64)
    private String authorizationTimeStamp;
    private Boolean isAuthorized = false;
    @ManyToOne
    @JoinColumn(name = "all_category_id")
    private AllCategory allCategory;
    @ManyToOne
    @JoinColumn(name = "all_sub_category_id")
    private AllSubCategory allSubCategory;
    @Column( length = 64)
    private String irregularity;
    @Column( length = 64)
    private String amount;
    @Column( length = 64)
    private String responsiblePerson;
    @Column(length = 64)
    private String actionPlanDueDate;
    @Column(length = 64)
    private String approvedBy;
    private Boolean actionTaken = false;
    private Boolean escalatedByManager = false;
    @ManyToOne
    @JoinColumn(name = "finance_status_id")
    private FinanceStatus financeStatus;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Branch team;

    @ManyToOne
    @JoinColumn(name = "sub_process_id")
    private SubProcess subProcess;

}


