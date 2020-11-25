package com.open.pesantren.config

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@Component
class AuthenticationManager(val jwtTokenProvider: JWTTokenProvider) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()

        return try {
            val username = jwtTokenProvider.getUsernameFromToken(authToken)
            if (!jwtTokenProvider.validateToken(authToken)) {
                return Mono.empty()
            }

            val claims = jwtTokenProvider.getAllClaimsFromToken(authToken)
            val roles = claims.get("roles", ArrayList::class.java)
            val authorities: MutableList<GrantedAuthority> = ArrayList()
            for (role in roles) {
                authorities.add(SimpleGrantedAuthority(role as String?))
            }

            Mono.just(UsernamePasswordAuthenticationToken(username, null, authorities))
        } catch (e: Exception) {
            Mono.empty()
        }
    }

}