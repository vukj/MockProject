package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.ConfirmationToken;

public interface ConfirmationTokenService {

    void save(ConfirmationToken confirmationToken);

    void delete(ConfirmationToken confirmationToken);

    ConfirmationToken findByEmail(String email);

    boolean existsByConfirmationToken(String confirmationToken);

    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
