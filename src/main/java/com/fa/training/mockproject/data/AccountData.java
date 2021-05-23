package com.fa.training.mockproject.data;

import com.fa.training.mockproject.entities.Employees;
import com.fa.training.mockproject.entities.UserAccounts;
import com.fa.training.mockproject.entities.UserRoles;
import com.fa.training.mockproject.enumeric.ActiveStatus;
import com.fa.training.mockproject.repositories.UserAccountsRepository;
import com.fa.training.mockproject.repositories.UserRolesRepository;
import com.fa.training.mockproject.services.DomainService;
import com.fa.training.mockproject.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountData {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private UserAccountsRepository userAccountsRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DomainService domainService;

    public void insertAccountData() {
        UserRoles userRolesLeader = new UserRoles();
        userRolesLeader.setUserRoleName("ROLE_LEADER");
        userRolesRepository.save(userRolesLeader);

        UserRoles userRolesManager = new UserRoles();
        userRolesManager.setUserRoleName("ROLE_MANAGER");
        userRolesRepository.save(userRolesManager);

        UserRoles userRolesStaff = new UserRoles();
        userRolesStaff.setUserRoleName("ROLE_STAFF");
        userRolesRepository.save(userRolesStaff);

        UserAccounts userAccountsManager = new UserAccounts();
        userAccountsManager.setEmail("manager@gmail.com");
        userAccountsManager.setPassword(passwordEncoder.encode("123123"));
        userAccountsManager.setActiveStatus(ActiveStatus.Activated);
        userAccountsManager.setUserRoles(userRolesManager);
        Employees employeesManager = new Employees();
        employeesManager.setFullName("Uchiha Sasuke");
        employeesManager.setAccount("manager");
        employeesManager.setUserAccounts(userAccountsManager);
        userAccountsManager.setEmployees(employeesManager);
        employeeService.saveEmployee(employeesManager);
        userAccountsRepository.save(userAccountsManager);

        UserAccounts userAccountsLeader = new UserAccounts();
        userAccountsLeader.setEmail("leader@gmail.com");
        userAccountsLeader.setPassword(passwordEncoder.encode("123123"));
        userAccountsLeader.setActiveStatus(ActiveStatus.Activated);
        userAccountsLeader.setUserRoles(userRolesLeader);
        Employees employeesLeader = new Employees();
        employeesLeader.setFullName("Uzumaki Naruto");
        employeesLeader.setAccount("leader");
        employeesLeader.setUserAccounts(userAccountsLeader);
        userAccountsLeader.setEmployees(employeesLeader);
        employeeService.saveEmployee(employeesLeader);
        userAccountsRepository.save(userAccountsLeader);

        UserAccounts userAccountsLeader1 = new UserAccounts();
        userAccountsLeader1.setEmail("leader1@gmail.com");
        userAccountsLeader1.setPassword(passwordEncoder.encode("123123"));
        userAccountsLeader1.setActiveStatus(ActiveStatus.Activated);
        userAccountsLeader1.setUserRoles(userRolesLeader);
        Employees employeesLeader1 = new Employees();
        employeesLeader1.setFullName("Uzumaki Boruto");
        employeesLeader1.setAccount("leader");
        employeesLeader1.setUserAccounts(userAccountsLeader1);
        userAccountsLeader1.setEmployees(employeesLeader1);
        employeeService.saveEmployee(employeesLeader1);
        userAccountsRepository.save(userAccountsLeader1);

        UserAccounts userAccountsStaff = new UserAccounts();
        userAccountsStaff.setEmail("staff@gmail.com");
        userAccountsStaff.setPassword(passwordEncoder.encode("123123"));
        userAccountsStaff.setActiveStatus(ActiveStatus.Activated);
        userAccountsStaff.setUserRoles(userRolesStaff);
        Employees employeesStaff = new Employees();
        employeesStaff.setFullName("Haruno Sakura");
        employeesStaff.setAccount("staff");
        employeesStaff.setUserAccounts(userAccountsStaff);
        userAccountsStaff.setEmployees(employeesStaff);
        employeeService.saveEmployee(employeesStaff);
        userAccountsRepository.save(userAccountsStaff);

        UserAccounts userAccountsStaff1 = new UserAccounts();
        userAccountsStaff1.setEmail("staff1@gmail.com");
        userAccountsStaff1.setPassword(passwordEncoder.encode("123123"));
        userAccountsStaff1.setActiveStatus(ActiveStatus.Activated);
        userAccountsStaff1.setUserRoles(userRolesStaff);
        Employees employeesStaff1 = new Employees();
        employeesStaff1.setFullName("Uchiha Sarada");
        employeesStaff1.setAccount("staff1");
        employeesStaff1.setUserAccounts(userAccountsStaff1);
        userAccountsStaff1.setEmployees(employeesStaff1);
        employeeService.saveEmployee(employeesStaff1);
        userAccountsRepository.save(userAccountsStaff1);

        UserAccounts userAccountsStaff2 = new UserAccounts();
        userAccountsStaff2.setEmail("staff2@gmail.com");
        userAccountsStaff2.setPassword(passwordEncoder.encode("123123"));
        userAccountsStaff2.setActiveStatus(ActiveStatus.Activated);
        userAccountsStaff2.setUserRoles(userRolesStaff);
        Employees employeesStaff2 = new Employees();
        employeesStaff2.setFullName("Uchiha Itachi");
        employeesStaff2.setAccount("staff2");
        employeesStaff2.setUserAccounts(userAccountsStaff2);
        userAccountsStaff2.setEmployees(employeesStaff2);
        employeeService.saveEmployee(employeesStaff2);
        userAccountsRepository.save(userAccountsStaff2);

        UserAccounts userAccountsStaff3 = new UserAccounts();
        userAccountsStaff3.setEmail("staff3@gmail.com");
        userAccountsStaff3.setPassword(passwordEncoder.encode("123123"));
        userAccountsStaff3.setActiveStatus(ActiveStatus.Activated);
        userAccountsStaff3.setUserRoles(userRolesStaff);
        Employees employeesStaff3 = new Employees();
        employeesStaff3.setFullName("Uchiha Madara");
        employeesStaff3.setAccount("staff3");
        employeesStaff3.setUserAccounts(userAccountsStaff3);
        userAccountsStaff3.setEmployees(employeesStaff3);
        employeeService.saveEmployee(employeesStaff3);
        userAccountsRepository.save(userAccountsStaff3);

        UserAccounts userAccountsStaff4 = new UserAccounts();
        userAccountsStaff4.setEmail("staff4@gmail.com");
        userAccountsStaff4.setPassword(passwordEncoder.encode("123123"));
        userAccountsStaff4.setActiveStatus(ActiveStatus.Activated);
        userAccountsStaff4.setUserRoles(userRolesStaff);
        Employees employeesStaff4 = new Employees();
        employeesStaff4.setFullName("Namikaze Minato");
        employeesStaff4.setAccount("staff");
        employeesStaff4.setUserAccounts(userAccountsStaff4);
        userAccountsStaff4.setEmployees(employeesStaff4);
        employeeService.saveEmployee(employeesStaff4);
        userAccountsRepository.save(userAccountsStaff4);
    }
}
