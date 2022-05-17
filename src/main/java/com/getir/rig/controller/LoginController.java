package com.getir.rig.controller;

import com.getir.rig.util.Constants;
import com.getir.rig.controller.dto.login.LoginRequest;
import com.getir.rig.controller.dto.login.LoginResponse;
import com.getir.rig.service.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(value = "/v1/login",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Login API", description = "Manages login-related processes")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(@NotNull @Valid @RequestBody LoginRequest loginRequest) {
        final var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());
        final var jwtToken = loginService.login(authenticationToken);

        return ResponseEntity.ok()
                .header(Constants.Security.AUTHORIZATION_HEADER_NAME, Constants.Security.BEARER_TOKEN_PREFIX + jwtToken)
                .body(LoginResponse.builder().token(jwtToken).build());
    }

}
