package com.open.pesantren.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@Configuration
@EnableWebFlux
class CORSFilter : WebFluxConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
    }
}