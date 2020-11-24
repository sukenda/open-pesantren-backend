package com.open.pesantren.service.impl

import com.open.pesantren.config.JWTTokenProvider
import com.open.pesantren.entity.User
import com.open.pesantren.exception.UserException
import com.open.pesantren.model.TokenRequest
import com.open.pesantren.model.UserRequest
import com.open.pesantren.repository.UserRepository
import com.open.pesantren.service.UserService
import com.open.pesantren.validation.ValidationUtil
import lombok.extern.slf4j.Slf4j
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import reactor.core.publisher.Mono

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@Slf4j
@Service(value = "userService")
class UserServiceImpl(val jwtTokenProvider: JWTTokenProvider,
                      val userRepository: UserRepository,
                      val passwordEncoder: PasswordEncoder,
                      val validationService: ValidationUtil)
    : UserService, UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User? = userRepository.findByName(username).block()
        Assert.notNull(user, "User must not be null!")

        return org.springframework.security.core.userdetails.User(
                user?.username, user?.password, user?.authorities)
    }

    override fun token(request: TokenRequest): Mono<User> {
        validationService.validate(request)

        return userRepository.findByName(request.username)
                .switchIfEmpty(Mono.error(UserException("Pastikan username dan password anda benar")))
                .flatMap { user ->
                    if (passwordEncoder.matches(request.password, user.password)) {
                        user.refreshToken = jwtTokenProvider.generateToken(user, user.roles!!, true)
                        user.accessToken = jwtTokenProvider.generateToken(user, user.roles!!, false)

                        userRepository.save(user)
                    }

                    Mono.error(UserException("Pastikan username dan password anda benar"))
                }
    }

    override fun refreshToken(refreshToken: String): Mono<User> {
        validationService.validate(refreshToken)

        return userRepository.findByRefreshToken(refreshToken)
                .switchIfEmpty(Mono.error(UserException("Pastikan refresh token yang anda kirim benar")))
                .flatMap { user ->
                    user.refreshToken = jwtTokenProvider.generateToken(user, user.roles!!, true)
                    user.accessToken = jwtTokenProvider.generateToken(user, user.roles!!, false)

                    userRepository.save(user)
                }
    }

    override fun signup(request: UserRequest): Mono<User> {
        validationService.validate(request)

        return userRepository.findByName(request.username!!)
                .defaultIfEmpty(User(name = request.username))
                .flatMap { current ->
                    if (current.id != null) {
                        return@flatMap Mono.error(UserException("Username sudah ada, silahkan gunakan username lain"))
                    }

                    val token: String = jwtTokenProvider.generateToken(current, request.roles!!, false)
                    val refresh: String = jwtTokenProvider.generateToken(current, request.roles, true)

                    val user = User(
                            name = request.username,
                            pass = passwordEncoder.encode(request.password),
                            email = request.email!!,
                            profile = request.profile!!,
                            enabled = true,
                            roles = request.roles,
                            refreshToken = refresh,
                            accessToken = token
                    )

                    userRepository.save(user)
                }
    }

}