package com.fa.training.mockproject.entities;

import com.fa.training.mockproject.enumeric.ActiveStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
public class Domains extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int domainId;

    @Column(columnDefinition = "varchar(255) default 'New Account'")
    @Length(max = 50)
    @NotNull
    private String domainName;//

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;

    @ManyToOne
    @JoinColumn(name = "domainTypes")
    private DomainTypes domainTypes;

    @OneToMany(mappedBy = "domains")
    private List<JobRoles> jobRolesList;

    @OneToMany(mappedBy = "domains")
    private List<Employees> employeesList;

    public Domains() {
    }

    public Domains(String domainName, DomainTypes domainTypes) {
        this.domainName = domainName;
        this.domainTypes = domainTypes;
    }
}
