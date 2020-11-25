package com.open.pesantren.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration(val authenticationManager: AuthenticationManager,
                            val securityContextRepository: SecurityContextRepository) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
        return http
                .exceptionHandling()
                .authenticationEntryPoint { swe: ServerWebExchange, _: AuthenticationException -> Mono.fromRunnable { swe.response.statusCode = HttpStatus.UNAUTHORIZED } }
                .accessDeniedHandler { swe: ServerWebExchange, _: AccessDeniedException -> Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN } }.and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers("/").permitAll()
                .pathMatchers(
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html**",
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html/**",
                        "favicon.ico"
                ).permitAll()
                .anyExchange().authenticated()
                .and().build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder(12)
    }

    @Bean
    fun customOpenAPI(): OpenAPI? {
        return OpenAPI()
                .components(Components()
                        .addSecuritySchemes("Token", SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .`in`(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(SecurityRequirement().addList("Token", listOf("read", "write")))
    }

}