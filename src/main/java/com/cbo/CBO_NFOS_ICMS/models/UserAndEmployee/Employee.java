package com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee;

import lombok.*;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;
    private String employeeFullName;
    private Long supervisorId;
    private String supervisorFullName;
    private String companyEntryDate;
    private String gender;
    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;
    @ManyToOne
    @JoinColumn(name="branch_id")
    private Branch branch;
    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;
    @ManyToOne
    @JoinColumn(name="sub_process_id")
    private SubProcess subProcess;
    @ManyToOne
    @JoinColumn(name="process_id")
    private Process process;
}

