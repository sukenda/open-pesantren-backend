package com.open.pesantren.config

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@Component
class SecurityContextRepository(val authenticationManager: AuthenticationManager)
    : ServerSecurityContextRepository {

    override fun save(swe: ServerWebExchange, sc: SecurityContext): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(serverWebExchange: ServerWebExchange): Mono<SecurityContext> {
        val request = serverWebExchange.request
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val authToken = authHeader.substring(7)
            val auth: Authentication = UsernamePasswordAuthenticationToken(authToken, authToken)

            authenticationManager.authenticate(auth).map { value -> SecurityContextImpl(value) }
        } else {
            Mono.empty()
        }
    }
}