package com.cbo.CBO_NFOS_ICMS.models.DCQ;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dishonored_cheques")
public class DishonoredCheque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Column(length = 64)
    private String datePresented;
    @Column(length = 64)
    private String fullNameOfDrawer;
    @Column(length = 64)
    private String accountNumber;
    @Column(length = 64)
    private String drawerAddress;
    @Column(length = 64)
    private String amountInBirr;
    @ManyToOne
    @JoinColumn(name = "cheque_type_id")
    private ChequeType chequeType;
    @ManyToOne
    @JoinColumn(name = "action_taken_id")
    private ActionTaken actionTaken;
    @Column(length = 15)
    private String tin;
    @Column(length = 64)
    private String chequeNumber;
    @Column(length = 64)
    private String nameOfBeneficiary;
    @Column(length = 64)
    private int frequency;
    /*@Column(nullable = false, length = 64)
    private Long outstandingBalance;*/
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "sub_process_id")
    private SubProcess subProcess;
}
