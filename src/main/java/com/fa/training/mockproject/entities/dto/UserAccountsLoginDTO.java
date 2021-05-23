package com.fa.training.mockproject.entities.dto;

import com.fa.training.mockproject.entities.CommonGenericClass;
import com.fa.training.mockproject.entities.Employees;
import com.fa.training.mockproject.entities.UserRoles;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Getter
@Setter
public class UserAccountsLoginDTO extends CommonGenericClass {

    private int id;

    private String email;

    private String password;

    private String newPassword;

    private String avatar;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date lastLogged;

    private Employees employees;

    private UserRoles userRoles;

    public UserAccountsLoginDTO() {
    }

    public UserAccountsLoginDTO(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }
}
