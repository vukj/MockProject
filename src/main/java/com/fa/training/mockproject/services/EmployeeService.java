package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.Employees;

import java.util.List;

public interface EmployeeService {

    List<Employees> findAll();

    Employees findEmployeeByUserAccountId(int id);

    void saveEmployee(Employees employees);

    void statusMemberProfile(int id);

    Employees findById(int id);

    void updateEmployee(Employees employees);
}
