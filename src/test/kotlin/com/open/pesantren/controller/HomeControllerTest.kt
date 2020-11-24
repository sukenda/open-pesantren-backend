package com.open.pesantren.controller

import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
internal class HomeControllerTest(@Autowired val client: WebTestClient) {

    @Test
    fun home() {
        client.get().uri("/")
                .exchange()
                .expectStatus().isOk
    }
}