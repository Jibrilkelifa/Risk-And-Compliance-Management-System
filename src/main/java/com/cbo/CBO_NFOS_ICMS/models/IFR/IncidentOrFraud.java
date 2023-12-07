package com.cbo.CBO_NFOS_ICMS.models.IFR;

import com.cbo.CBO_NFOS_ICMS.models.AllCategory;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.SuspectedFraudsterProfession;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Team;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Data

@NoArgsConstructor
@Entity
@Table(name = "incidentFraudReports")
public class IncidentOrFraud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( updatable = false)
    private Long id;
    @Column( length = 64)
    private String suspectedFraudsterName ;
    @Column( length = 64)
    private String suspectedFraudsterAddress ;
    @ManyToOne
    @JoinColumn(name = "fraud_type_id")
    private FraudType fraudType;
    @ManyToOne
    @JoinColumn(name = "case_status_id")
    private CaseStatus caseStatus;
    @ManyToOne
    @JoinColumn(name = "suspected_fraudster_profession_id")
    private SuspectedFraudsterProfession suspectedFraudsterProfession;
    @ManyToOne
    @JoinColumn(name = "all_category_id")
    private AllCategory allCategory;
    @Column( length = 64)
    private String fraudCause;
    @Column( length = 64)
    private String caseId;
    @Column( length = 64)
    private String preparedBy;
    @Column( length = 64)
    private String authorizedBy;
    @Column( length = 64)
    private String authorizationTimeStamp;
    @Column( length = 64)
    private String fraudAmount;
    @Column( length = 64)
    private String provisionHeld;
    @Column( length = 64)
    private String fraudOccurrenceDate;
    @Column( length = 64)
    private  String fraudDetectionDate;
    @Column( length = 64)
    private String  reasonForDelay;
    @Column( length = 64)
    private String fraudOccurrencePlace;
    private String fileName;
    private String filePath;
    private byte[] fileData;
    @Column( length = 64)
    private String fraudCommittingTechnique;
    @Column( length = 64)
    private String actionTaken;
    @Column( length = 64)
    private String amountRecovered;
    @Column( length = 64)
    private String otherFraudCategory;
    @Column( length = 64)
    private String otherFraudType;
    @Column( length = 64)
    private String otherSuspectedFraudsterProfession;
    @Column( length = 64)
    private String reasonForFailedFraudAttempt;
    private String addedByRole;
    @Column( length = 64)
    private String otherComment;
    private Boolean isAuthorized= false;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @JoinColumn(name = "branch_id")
    private Boolean isAuthorized= false;
//    private Boolean isWrittenOff= false;
    @ManyToOne
    @JoinColumn(name = "sub_process_id")
    private SubProcess subProcess;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
