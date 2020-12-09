package com.open.pesantren.service.impl

import com.open.pesantren.config.JWTTokenProvider
import com.open.pesantren.entity.User
import com.open.pesantren.exception.DataNotFoundException
import com.open.pesantren.exception.InvalidUserPasswordException
import com.open.pesantren.exception.UserExistException
import com.open.pesantren.model.*
import com.open.pesantren.repository.UserRepository
import com.open.pesantren.service.UserService
import com.open.pesantren.validation.ValidationUtil
import lombok.extern.slf4j.Slf4j
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
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
                .switchIfEmpty(Mono.error(InvalidUserPasswordException("Pastikan username dan password anda benar")))
                .flatMap { user ->
                    if (passwordEncoder.matches(request.password, user.password)) {

                        val refresh = tokenProvider.generateToken(user.username, user.roles, true)
                        val token = tokenProvider.generateToken(user.username, user.roles, false)

                        user.refreshToken = refresh
                        return@flatMap userRepository.save(user)
                                .flatMap {
                                    Mono.just(TokenResponse(
                                            accessToken = token,
                                            refreshToken = refresh,
                                            expiration = tokenProvider.getExpirationDateFromToken(token).time))
                                }
                    }

                    Mono.error(InvalidUserPasswordException("Pastikan username dan password anda benar"))
                }
    }

    override fun refreshToken(refreshToken: String): Mono<TokenResponse> {
        validationService.validate(refreshToken)

        return userRepository.findByRefreshToken(refreshToken)
                .switchIfEmpty(Mono.error(InvalidUserPasswordException("Pastikan refresh token yang anda kirim benar")))
                .flatMap { user ->

                    val refresh = tokenProvider.generateToken(user.username, user.roles, true)
                    val token = tokenProvider.generateToken(user.username, user.roles, false)

                    user.refreshToken = refresh
                    userRepository.save(user).flatMap {
                        Mono.just(TokenResponse(
                                accessToken = token,
                                refreshToken = refresh,
                                expiration = tokenProvider.getExpirationDateFromToken(token).time))
                    }
                }
    }

    override fun signup(request: UserRequest): Mono<UserResponse> {
        validationService.validate(request)

        val newUser = User(
                id = null,
                refreshToken = null,
                image = null,
                name = request.username!!,
                pass = passwordEncoder.encode(request.password),
                email = request.email!!,
                profile = request.profile!!,
                roles = request.roles!!,
                enabled = true
        )

        return userRepository.findByName(request.username)
                .defaultIfEmpty(newUser)
                .flatMap { current ->
                    if (current.id != null) {
                        Mono.error(UserExistException("Username sudah ada, silahkan gunakan username lain"))

                    } else {
                        val refresh: String = tokenProvider.generateToken(request.username, request.roles, true)

                        newUser.id = UUID.randomUUID().toString()
                        newUser.refreshToken = refresh

                        userRepository.save(newUser)
                                .flatMap {
                                    Mono.just(userToResponse(it))
                                }
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
                            status = HttpStatus.OK.name,
                            code = HttpStatus.OK.value(),
                            rows = count,
                            data = users.map { userToResponse(it) }
                    )
                }
    }

    override fun findById(id: String): Mono<UserResponse> {
        return findByIdOrThrow(id)
                .flatMap {
                    Mono.just(userToResponse(it))
                }
    }

    override fun disable(id: String): Mono<UserResponse> {
        return findByIdOrThrow(id)
                .flatMap { it ->

                    it.enabled = false
                    userRepository.save(it)
                            .flatMap { Mono.just(userToResponse(it)) }
                }
    }

    override fun updateRole(id: String, roles: MutableSet<String>): Mono<UserResponse> {
        return findByIdOrThrow(id)
                .flatMap { it ->

                    it.roles = roles
                    userRepository.save(it)
                            .flatMap { Mono.just(userToResponse(it)) }
                }
    }

    fun findByIdOrThrow(id: String): Mono<User> {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(DataNotFoundException("Data tidak ditemukan")))
                .flatMap { Mono.just(it) }
    }

    fun userToResponse(user: User): UserResponse {
        return UserResponse(user.id!!, user.name, user.email, user.profile, user.roles, user.enabled)
    }

}