package com.vivatech.ums_api_gateway.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "privilegegroup")
public class PrivilegeGroup {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "privilegeGroup")
    @JsonManagedReference
    private Collection<Privilege> privileges;
}
