package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.Employees;
import com.fa.training.mockproject.entities.UserAccounts;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fa.training.mockproject.repositories.EmployeesRepository;
import com.fa.training.mockproject.repositories.UserAccountsRepository;
import com.fa.training.mockproject.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private UserAccountsRepository userAccountsRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    /**
     * get list Employees
     *
     * @return
     */
    @Override
    public List<Employees> findAll() {
        return employeesRepository.findAll();
    }

    /**
     * Find an Employees by User Account ID
     *
     * @param id
     * @return
     */
    @Override
    public void updateEmployee(Employees employees) {
        employeesRepository.save(employees);
    }

    @Override
    public Employees findEmployeeByUserAccountId(int id) {
        return null;
    }

    /**
     * Save an Employees
     *
     * @param employees
     */
    @Override
    public void saveEmployee(Employees employees) {
        employeesRepository.save(employees);
    }

    @Override
    public void statusMemberProfile(int id) {
        UserAccounts userAccounts = userAccountsRepository.findByUserId(id);
        if (userAccounts.getActiveStatus() == ActiveStatus.Activated) {
            userAccounts.setActiveStatus(ActiveStatus.Inactive);
        } else {
            userAccounts.setActiveStatus(ActiveStatus.Activated);
        }
        userAccountsRepository.save(userAccounts);
    }

    /**
     * Finf an Employees by ID
     *
     * @param id
     * @return
     */
    public Employees findById(int id) {
        return employeesRepository.findByEmployeeId(id);
    }
}
