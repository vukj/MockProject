package com.fa.training.mockproject.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
public class UserRoles extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userRoleId;

    @Length(max = 50)
    @NotNull
    private String userRoleName;

    @OneToMany(mappedBy = "userRoles", cascade = CascadeType.ALL)
    private List<UserAccounts> userAccountsList;

}
