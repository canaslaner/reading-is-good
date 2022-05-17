package com.getir.rig.controller;

import com.getir.rig.controller.dto.customer.RegisterRequest;
import com.getir.rig.security.model.enums.Authority;
import com.getir.rig.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/v1/backoffice",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Backoffice User API", description = "Manages backoffice user-related processes")
@RequiredArgsConstructor
public class BackofficeController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registers new backoffice user",
            description = "Register new backoffice user to log in and manage customers, orders etc.")
    @ApiResponse(responseCode = "201", description = "User successfully registered.")
    @ApiResponse(responseCode = "400", description = "One or more field is/are invalid.")
    public void register(@Valid @RequestBody final RegisterRequest registerRequest) {
        userService.register(registerRequest, Authority.BO_USER);
    }
}
