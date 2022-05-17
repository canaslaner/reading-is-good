package com.getir.rig.service;

import com.getir.rig.controller.dto.customer.RegisterRequest;
import com.getir.rig.exception.EmailAlreadyUsedException;
import com.getir.rig.exception.RigGenericException;
import com.getir.rig.security.model.User;
import com.getir.rig.security.model.enums.Authority;
import com.getir.rig.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(final RegisterRequest request, final Authority authority) {
        userRepository.findByEmailAndActive(request.getEmail(), true)
                .ifPresent(existingUser -> {
                    throw new EmailAlreadyUsedException();
                });

        final var newCustomer = User.builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .authority(authority)
                .active(true)
                .build();

        return userRepository.save(newCustomer);
    }

    @Override
    public User findActiveByEmail(final String email) {
        return userRepository
                .findByEmailAndActive(email, true)
                .orElseThrow(() -> new RigGenericException("exception.userNotFound"));
    }
}
