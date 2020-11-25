package com.open.pesantren.controller

import com.open.pesantren.model.*
import com.open.pesantren.service.UserService
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "API description for auth")
class AuthController(val userService: UserService) {

    @SecurityRequirements
    @PostMapping(value = ["/token"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun token(@RequestBody request: TokenRequest): Mono<TokenResponse> {
        return userService.token(request)
    }

    @SecurityRequirements
    @PostMapping(value = ["/refresh-token"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun refreshToken(@RequestBody request: RefreshTokenRequest): Mono<TokenResponse> {
        return userService.refreshToken(request.refreshToken)
    }

    @SecurityRequirements
    @PostMapping(value = ["/signup"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun signup(@RequestBody request: UserRequest): Mono<RestResponse<UserResponse>> {
        return userService.signup(request).flatMap {
            Mono.just(RestResponse(
                    status = HttpStatus.CREATED.name,
                    code = HttpStatus.CREATED.value(),
                    data = it)
            )
        }
    }

}