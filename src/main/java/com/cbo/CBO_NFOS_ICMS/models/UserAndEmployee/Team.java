package com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teams")
public class Team {
    @Id
    private Long id;
    private String externalName;


}