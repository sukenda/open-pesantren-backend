package com.open.pesantren.controller

import com.open.pesantren.model.*
import com.open.pesantren.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "API description for User")
class UserController(val userService: UserService) {

    @GetMapping(produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun find(@RequestParam("profile", required = false, defaultValue = "") profile: String,
             @RequestParam("roles", required = false, defaultValue = "") roles: String,
             @RequestParam("page") page: Int,
             @RequestParam("size") size: Int): Mono<RestResponse<List<UserResponse>>> {

        return userService.find(profile, roles, page, size)
    }

    @GetMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun findById(@PathVariable("id") id: String): Mono<RestResponse<UserResponse>> {
        return userService.findById(id)
    }

}