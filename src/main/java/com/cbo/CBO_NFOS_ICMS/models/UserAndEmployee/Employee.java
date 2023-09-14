package com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,
            generator = "employee_sequence")
    private Long id;
    private Long employeeId;
    private String fullName;
    private Boolean active;
    private String jobTitle;
    @ManyToOne
    private OrganizationalUnit organizationalUnit;
    private String phoneNumber;
    private String personalEmail;
    private String companyEmail;
    private String gender;
    private String birthDate;
    private String employeeImage;
    private String signatureImage;

    @OneToOne(mappedBy = "employee")
    @JsonIgnore
    private User user;

    public Employee(Long employeeId, String fullName, String jobTitle, OrganizationalUnit organizationalUnit, String personalEmail, String companyEmail, String phoneNumber, String employeeImage,  String signatureImage, String gender, String birthDate) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
        this.organizationalUnit = organizationalUnit;
        this.personalEmail = personalEmail;
        this.companyEmail = companyEmail;
        this.phoneNumber = phoneNumber;
        this.employeeImage = employeeImage;
        this.signatureImage = signatureImage;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    @Transient
    public String getSignImagePath() {
        if (signatureImage == null || id == null) return null;
        return "/user-photos/employee/" + id + "/" + signatureImage;
    }
}

