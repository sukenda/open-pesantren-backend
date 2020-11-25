package com.open.pesantren.controller

import com.open.pesantren.model.TokenRequest
import com.open.pesantren.model.UserRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
internal class AuthControllerTest(@Autowired val client: WebTestClient) {

    @Test
    fun register() {
        val userRequest = UserRequest(
                username = "Kenda",
                password = "Kenda",
                email = "Kenda@gmail.com",
                profile = "Kenda Profile",
                roles = mutableSetOf("ADMIN"))

        client.post().uri("/auth/signup")
                .bodyValue(userRequest)
                .accept(MediaType.valueOf(MediaType.APPLICATION_NDJSON_VALUE))
                .exchange()
                .expectStatus().isOk
                .expectBody()
    }

    @Test
    fun token() {
        val userRequest = TokenRequest(
                username = "Kenda",
                password = "Kenda")

        client.post().uri("/auth/token")
                .bodyValue(userRequest)
                .accept(MediaType.valueOf(MediaType.APPLICATION_NDJSON_VALUE))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty
                .jsonPath("$.refreshToken").isNotEmpty
    }

}