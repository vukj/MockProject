package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesRepository extends JpaRepository<Employees, Integer> {
    Employees findByEmployeeId(int id);

    Employees findEmployeesByUserAccounts_UserId(int id);
}
