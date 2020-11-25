package com.open.pesantren.service

import com.open.pesantren.model.*
import reactor.core.publisher.Mono

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
interface UserService {

    fun token(request: TokenRequest): Mono<TokenResponse>

    fun refreshToken(refreshToken: String): Mono<TokenResponse>

    fun signup(request: UserRequest): Mono<UserResponse>

    fun find(profile: String, roles: String, page: Int = 0, size: Int = 20): Mono<RestResponse<List<UserResponse>>>

    fun findById(id: String): Mono<RestResponse<UserResponse>>

}