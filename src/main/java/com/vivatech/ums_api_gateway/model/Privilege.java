package com.vivatech.ums_api_gateway.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Builder
@Table(name = "privilege")
public class Privilege {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String name;
    private String displayName;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "privilege_group_id")
    private PrivilegeGroup privilegeGroup;

    public Privilege() {
        super();
    }

    public Privilege(final String name) {
        super();
        this.name = name;
    }
}
