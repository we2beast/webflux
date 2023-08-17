package com.viliavin.webflux.controller;

import com.viliavin.webflux.domain.model.ResourceObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureWebTestClient
public class ResourceControllerTest {

    @Autowired
    private WebTestClient rest;

    @Test
    void createResourceThenReturnID() throws Exception {
        rest.post().uri("/resource")
            .contentType(APPLICATION_JSON)
            .bodyValue("{ \"id\": 1, \"value\": \"val1\", \"path\": \"path1\" }")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Integer.class).isEqualTo(1);
    }

    @Test
    void getResourceWhenResourceCreated() throws Exception {
        rest.post().uri("/resource")
            .contentType(APPLICATION_JSON)
            .bodyValue("{ \"id\": 1, \"value\": \"val1\", \"path\": \"path1\" }")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Integer.class).isEqualTo(1);

        rest.get().uri("/resource/1")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody().jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void getResourceWhenNotFound() throws Exception {
        rest.get().uri("/resource/2")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody().isEmpty();
    }

    @Test
    void searchResourcesByValue() throws Exception {
        rest.get().uri("/resource/search?q=val")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(ResourceObject.class);
    }

    @Test
    void searchResources() throws Exception {
        rest.get().uri("/resource/searchByPath?path=path1")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(ResourceObject.class);
    }

}
