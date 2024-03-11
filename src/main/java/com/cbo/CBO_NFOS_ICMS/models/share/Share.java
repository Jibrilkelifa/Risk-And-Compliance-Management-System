package com.cbo.CBO_NFOS_ICMS.models.share;

import com.cbo.CBO_NFOS_ICMS.models.AllIrregularity;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@NoArgsConstructor
@Entity
@Table(name = "shares")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Column(length = 64)
    private String date;
    @Column(length = 64)
    private String caseId;
    @Column(length = 64)
    private String amountInvolved;
    @Column(length = 64)
    private String responsiblePerson;
    @Column(length = 64)
    private String shareNumber;
    @Column(length = 64)
    private String shareHoldersName;

    @ManyToOne
    @JoinColumn(name = "share_status_id")
    private ShareStatus shareStatus;
    @ManyToOne
    @JoinColumn(name = "irregularity_id")
    private AllIrregularity irregularity;
    @Column(length = 64)
    private String otherIrregularity;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "sub_Process_id")
    private SubProcess subProcess;
}
