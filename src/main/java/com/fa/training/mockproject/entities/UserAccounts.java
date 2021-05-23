package com.fa.training.mockproject.entities;

import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
@Entity
public class UserAccounts extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Length(max = 100)
    @Email
    @Pattern(regexp = "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$")
    @Column(unique = true)
    private String email;

    @Length(max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ActiveStatus activeStatus;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date lastLogged;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "employees")
    @JsonIgnoreProperties(value = {"competencyRankingProfilesList", "competencyResultsOfLeaderList"})
    private Employees employees;

    @ManyToOne
    @JsonIgnoreProperties(value = {"userAccountsList"})
    private UserRoles userRoles;
}
