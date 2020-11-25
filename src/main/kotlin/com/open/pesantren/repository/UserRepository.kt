package com.open.pesantren.repository

import com.open.pesantren.entity.User
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveMongoRepository<User, String> {

    fun findByName(name: String): Mono<User>

    fun findByRefreshToken(refreshToken: String): Mono<User>

    fun findByProfileContainingAndRolesContaining(profile: String, roles: String, pageable: Pageable): Flux<User>

    fun existsByName(name: String): Mono<Boolean>

}