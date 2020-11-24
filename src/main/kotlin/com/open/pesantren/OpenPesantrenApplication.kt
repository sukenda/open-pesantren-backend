package com.open.pesantren

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "APIs", version = "1.0", description = "Documentation APIs v1.0"))
@EnableMongoAuditing
class OpenPesantrenApplication

fun main(args: Array<String>) {
	runApplication<OpenPesantrenApplication>(*args)
}
