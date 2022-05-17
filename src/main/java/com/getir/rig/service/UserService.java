package com.getir.rig.service;

import com.getir.rig.controller.dto.customer.RegisterRequest;
import com.getir.rig.security.model.enums.Authority;
import com.getir.rig.security.model.User;

public interface UserService {
    User register(RegisterRequest request, Authority authority);

    User findActiveByEmail(String email);
}
