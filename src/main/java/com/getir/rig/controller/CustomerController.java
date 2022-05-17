package com.getir.rig.controller;

import com.getir.rig.controller.dto.customer.RegisterRequest;
import com.getir.rig.controller.dto.customer.UserInfoResponse;
import com.getir.rig.exception.RigGenericException;
import com.getir.rig.security.model.enums.Authority;
import com.getir.rig.service.UserService;
import com.getir.rig.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/v1/customer",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Customer API", description = "Manages customer-related processes")
@RequiredArgsConstructor
public class CustomerController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registers new customer",
            description = "Register new customer to log in, order etc.")
    @ApiResponse(responseCode = "201", description = "User successfully registered.")
    @ApiResponse(responseCode = "400", description = "One or more field is/are invalid.")
    public void register(@Valid @RequestBody final RegisterRequest registerRequest) {
        userService.register(registerRequest, Authority.CUSTOMER);
    }

    @GetMapping
    @Operation(summary = "Returns customer detail",
            description = "Returns detail information about the customer",
            security = @SecurityRequirement(name = "bearer authorization token"))
    public UserInfoResponse getCustomerInfo() {
        final var email = SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new RigGenericException("exception.userNotFound"));
        final var user = userService.findActiveByEmail(email);
        return UserInfoResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .build();
    }
}
