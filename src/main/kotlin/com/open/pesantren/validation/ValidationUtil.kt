package com.open.pesantren.validation

import org.springframework.stereotype.Component
import javax.validation.ConstraintViolationException
import javax.validation.Validator

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@Component
class ValidationUtil(val validator: Validator) {

    fun validate(any: Any) {
        val result = validator.validate(any)
        if (result.size != 0) {
            throw ConstraintViolationException(result)
        }
    }
}