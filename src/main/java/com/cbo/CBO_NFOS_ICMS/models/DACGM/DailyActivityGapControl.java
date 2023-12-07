package com.cbo.CBO_NFOS_ICMS.models.DACGM;

import com.cbo.CBO_NFOS_ICMS.models.AllIrregularity;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@NoArgsConstructor
@Entity
@Table(name = "daily_activities_gap_control")
public class DailyActivityGapControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Column(length = 64)
    private String date;
    @Column(length = 64)
    private String preparedBy;
    @Column(length = 64)
    private String caseId;
    @Column(length = 64)
    private String accountNumber;
    @Column(length = 64)
    private String accountName;
    @Column(length = 64)
    private String amountInvolved;
    @Column(length = 64)
    private String responsiblePerson;
    @Column(length = 64)
    private String actionPlanDueDate;
    @Column(length = 64)
    private String approvedBy;
    private Boolean escalatedByManager = false;

    private Boolean actionTaken = false;

    @ManyToOne
    @JoinColumn(name = "activity_status_id")
    private ActivityStatus activityStatus;
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

