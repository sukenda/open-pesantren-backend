package com.open.pesantren.service.impl

import com.open.pesantren.config.JWTTokenProvider
import com.open.pesantren.entity.User
import com.open.pesantren.exception.DataNotFoundException
import com.open.pesantren.exception.UserException
import com.open.pesantren.model.*
import com.open.pesantren.repository.UserRepository
import com.open.pesantren.service.UserService
import com.open.pesantren.validation.ValidationUtil
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import reactor.core.publisher.Mono
import java.util.*

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@Slf4j
@Service(value = "userService")
class UserServiceImpl(val tokenProvider: JWTTokenProvider,
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

    override fun token(request: TokenRequest): Mono<TokenResponse> {
        validationService.validate(request)

        return userRepository.findByName(request.username)
                .switchIfEmpty(Mono.error(UserException("Pastikan username dan password anda benar")))
                .flatMap { user ->
                    if (passwordEncoder.matches(request.password, user.password)) {

                        val refreshToken = tokenProvider.generateToken(user.username, user.roles, true)
                        val accessToken = tokenProvider.generateToken(user.username, user.roles, false)

                        user.refreshToken = refreshToken
                        return@flatMap userRepository.save(user)
                                .flatMap { Mono.just(TokenResponse(accessToken = accessToken, refreshToken = refreshToken)) }
                    }

                    Mono.error(UserException("Pastikan username dan password anda benar"))
                }
    }

    override fun refreshToken(refreshToken: String): Mono<TokenResponse> {
        validationService.validate(refreshToken)

        return userRepository.findByRefreshToken(refreshToken)
                .switchIfEmpty(Mono.error(UserException("Pastikan refresh token yang anda kirim benar")))
                .flatMap { user ->

                    val refresh = tokenProvider.generateToken(user.username, user.roles, true)
                    val token = tokenProvider.generateToken(user.username, user.roles, false)

                    user.refreshToken = refresh
                    userRepository.save(user).flatMap {
                        Mono.just(TokenResponse(accessToken = token, refreshToken = refresh))
                    }
                }
    }

    override fun signup(request: UserRequest): Mono<UserResponse> {
        validationService.validate(request)

        return userRepository.existsByName(request.username!!)
                .flatMap { current ->
                    if (current) {
                        return@flatMap Mono.error(UserException("Username sudah ada, silahkan gunakan username lain"))
                    }

                    val refresh: String = tokenProvider.generateToken(request.username, request.roles!!, true)
                    val user = User(
                            id = UUID.randomUUID().toString(),
                            name = request.username,
                            pass = passwordEncoder.encode(request.password),
                            email = request.email!!,
                            profile = request.profile!!,
                            enabled = true,
                            roles = request.roles,
                            refreshToken = refresh
                    )

                    userRepository.save(user)
                            .flatMap {
                                Mono.just(UserResponse(
                                        id = it.id,
                                        username = it.username,
                                        active = it.enabled,
                                        roles = it.roles,
                                        email = it.email,
                                        profile = it.profile))
                            }
                }

    }

    override fun find(profile: String, roles: String, page: Int, size: Int): Mono<RestResponse<List<UserResponse>>> {
        var count = 0

        return userRepository.count()
                .map { row -> count = row.toInt() }
                .flatMapMany { userRepository.findByProfileContainingAndRolesContaining(profile, roles, PageRequest.of(page, size)) }
                .collectList()
                .map { users ->
                    RestResponse(
                            status = "OK",
                            code = 200,
                            rows = count,
                            data = users.map { UserResponse(it.id, it.username, it.email, it.profile, it.roles, it.enabled) }
                    )
                }
    }

    override fun findById(id: String): Mono<RestResponse<UserResponse>> {
        return findByIdOrThrow(id)
                .flatMap {
                    Mono.just(RestResponse(
                            rows = 1,
                            status = "OK",
                            code = 200,
                            data = UserResponse(it.id, it.name, it.email, it.profile, it.roles, it.enabled)
                    ))
                }
    }

    fun findByIdOrThrow(id: String): Mono<User> {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(DataNotFoundException("Data tidak ditemukan")))
                .flatMap { Mono.just(it) }
    }
}