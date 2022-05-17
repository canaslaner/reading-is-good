package com.getir.rig.controller;

import com.getir.rig.controller.dto.customer.RegisterRequest;
import com.getir.rig.service.UserService;
import com.getir.testutil.MvcUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class BackofficeControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testRegister_whenRequestIsValid_thenCreateUserSuccessfully() throws Exception {
        // given
        final var request = RegisterRequest.builder().firstName("Alan").lastName("Turing")
                .password("12345678").email("alan.turing@scientist.com").build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/backoffice/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(MvcUtils.createJsonContent(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @ParameterizedTest
    @CsvSource({
            "Alan, Turing, 12345678, , email, must not be empty",
            "Alan, Turing, 12345678, x, email, must be a well-formed email address",
            ", Turing, 12345678, alan.turing@scientist.com, firstName, must not be empty",
            "Alan, , 12345678, alan.turing@scientist.com, lastName, must not be empty",
            "Alan, Turing, , alan.turing@scientist.com, password, must not be empty",
            "Alan, Turing, 123, alan.turing@scientist.com, password, length must be between 5 and 10",
            "Alan, Turing, 11122233344, alan.turing@scientist.com, password, length must be between 5 and 10",
    })
    void testRegister_whenFieldIsInvalid_thenReturnBadRequest(final String firstName, final String lastname,
                                                              final String password, final String email,
                                                              final String expectedFieldValue,
                                                              final String expectedMessageValue) throws Exception {
        // given
        final var request = RegisterRequest.builder().firstName(firstName).lastName(lastname)
                .password(password).email(email).build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/backoffice/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(MvcUtils.createJsonContent(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations[0].field").value(expectedFieldValue))
                .andExpect(MockMvcResultMatchers.jsonPath("$.violations[0].message").value(expectedMessageValue));
    }
}
