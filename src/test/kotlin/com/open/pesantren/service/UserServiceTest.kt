package com.open.pesantren.service

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.validation.Validation.buildDefaultValidatorFactory
import javax.validation.Validator
import javax.validation.ValidatorFactory


@SpringBootTest
@DisplayName("User Service Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserServiceTest(@Autowired val service: UserService) {

    private var validatorFactory: ValidatorFactory? = null

    private var validator: Validator? = null

    @BeforeAll
    fun open() {
        validatorFactory = buildDefaultValidatorFactory()
        validator = validatorFactory!!.validator
    }

    @AfterAll
    fun close() {
        validatorFactory!!.close()
    }


}