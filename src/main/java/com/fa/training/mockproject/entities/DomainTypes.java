package com.fa.training.mockproject.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class DomainTypes extends CommonGenericClass {

    @Id
    private short domainTypeId;

    @Column(unique = true)
    private String domainTypeName;

    @OneToMany(mappedBy = "domainTypes", cascade = CascadeType.ALL)
    private List<Domains> domainsList;

    public DomainTypes() {
    }

    public DomainTypes(String domainTypeName) {
        this.domainTypeName = domainTypeName;
    }
}
