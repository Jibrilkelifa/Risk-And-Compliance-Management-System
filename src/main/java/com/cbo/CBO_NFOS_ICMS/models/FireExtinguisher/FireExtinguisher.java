package com.cbo.CBO_NFOS_ICMS.models.FireExtinguisher;

import com.cbo.CBO_NFOS_ICMS.models.AllCategory;
import com.cbo.CBO_NFOS_ICMS.models.AllSubCategory;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.Status;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "FireExtinguisher_table")
public class FireExtinguisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Column(length = 64)
    private String extinguisherSerialNumber;
    @Column(length = 64)
    private Number size;
    @Column(length = 64)
    private String inspectionDate;
    @Column(length = 64)
    private String nextInspectionDate;
    @Column(length = 64)
    private Number daysLeftForInspection;
    private Boolean isAuthorized = false;
    @Column(length = 64)
    private String status;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;


    @ManyToOne
    @JoinColumn(name = "sub_process_id")
    private SubProcess subProcess;

}


