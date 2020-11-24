package com.open.pesantren.service

import com.open.pesantren.entity.User
import com.open.pesantren.model.TokenRequest
import com.open.pesantren.model.UserRequest
import reactor.core.publisher.Mono

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
interface UserService {

    fun token(request: TokenRequest): Mono<User>

    fun refreshToken(refreshToken: String): Mono<User>

    fun signup(request: UserRequest): Mono<User>

}