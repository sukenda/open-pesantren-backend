package com.open.pesantren.controller

import com.open.pesantren.model.TokenRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
internal class UserControllerTest(@Autowired val client: WebTestClient) {

    @Test
    fun token() {
        val tokenRequest = TokenRequest(
                username = "Kenda",
                password = "Kenda")

        client.post().uri("/auth/token")
                .bodyValue(tokenRequest)
                .accept(MediaType.valueOf(MediaType.APPLICATION_NDJSON_VALUE))
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun findById() {
        client.get().uri("/users/{id}", "f2434821-5d7d-4056-ae3f-503fa9d2bf8b")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjA2Mjg1MTg1MDc2LCJyb2xlcyI6WyJBRE1JTiJdLCJ1c2VybmFtZSI6IktlbmRhIiwic3ViIjoiS2VuZGEiLCJpYXQiOjE2MDYyODUxODUsImV4cCI6MTYwNjMxMzk4NX0.fdj7rN9_y1v8ZNg5ZI2cLxHAxOAys3n_6p7goukAUdrWarQ8cC1NN4fMgEhIYjBE8crD1cVhl1HyyeZZs993yA")
                .accept(MediaType.valueOf(MediaType.APPLICATION_NDJSON_VALUE))
                .exchange()
                .expectStatus().isOk
                .expectBody()
    }

    @Test
    fun find() {
        client.get().uri("users")
                .accept(MediaType.valueOf(MediaType.APPLICATION_NDJSON_VALUE))
                .exchange()
                .expectStatus().isOk
                .expectBody()
    }

}