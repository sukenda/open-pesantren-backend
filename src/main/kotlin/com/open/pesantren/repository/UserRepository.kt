package com.open.pesantren.repository

import com.open.pesantren.entity.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveMongoRepository<User, String> {

    fun findByName(name: String): Mono<User>

    fun findByRefreshToken(refreshToken: String): Mono<User>
}