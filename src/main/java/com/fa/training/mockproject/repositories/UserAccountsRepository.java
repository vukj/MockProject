package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.UserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountsRepository extends JpaRepository<UserAccounts, Integer> {
    UserAccounts findByEmail(String email);

    boolean existsByEmail(String email);

    UserAccounts findByUserId(int id);
}
