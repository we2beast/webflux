package com.viliavin.webflux.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureWebTestClient
public class CheckPermissionControllerTest {

    @Autowired
    private WebTestClient rest;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getUserWhenAuthAsUser() throws Exception {
        rest.get().uri("/user")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class).isEqualTo("User permission");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAdminWhenAuthAsUser() throws Exception {
        rest.get().uri("/admin")
            .exchange()
            .expectStatus()
            .isForbidden();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUserWhenAuthAsAdmin() throws Exception {
        rest.get().uri("/user")
            .exchange()
            .expectStatus()
            .isForbidden();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAdminWhenAuthAsAdmin() throws Exception {
        rest.get().uri("/admin")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class).isEqualTo("Admin permission");
    }

}
