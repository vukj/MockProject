package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);

    ConfirmationToken findByEmail(String email);

    boolean existsByConfirmationToken(String confirmationToken);
}
