package com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee;

import javax.persistence.*;
import lombok.*;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_processes")
public class SubProcess implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
}