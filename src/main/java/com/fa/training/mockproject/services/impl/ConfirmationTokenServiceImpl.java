package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.ConfirmationToken;
import com.fa.training.mockproject.repositories.ConfirmationTokenRepository;
import com.fa.training.mockproject.services.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    /**
     * Create a Token to send email to user
     *
     * @param confirmationToken
     */
    @Override
    public void save(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    /**
     * Delete a Token after confirmed
     *
     * @param confirmationToken
     */
    @Override
    public void delete(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }

    /**
     * Find a Token by email
     *
     * @param email
     * @return
     */
    @Override
    public ConfirmationToken findByEmail(String email) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByEmail(email);
        return confirmationToken;
    }

    /**
     * Check exits Token
     *
     * @param confirmationToken
     * @return
     */
    @Override
    public boolean existsByConfirmationToken(String confirmationToken) {
        if (confirmationTokenRepository.existsByConfirmationToken(confirmationToken)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find a ConfirmationToken by Token
     *
     * @param confirmationToken
     * @return
     */
    @Override
    public ConfirmationToken findByConfirmationToken(String confirmationToken) {
        return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    }
}
