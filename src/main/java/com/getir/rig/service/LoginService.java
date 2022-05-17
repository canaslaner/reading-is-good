package com.getir.rig.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface LoginService {
    String login(UsernamePasswordAuthenticationToken authenticationToken);
}
