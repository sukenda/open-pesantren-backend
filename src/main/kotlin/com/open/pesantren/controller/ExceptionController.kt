package com.open.pesantren.controller

import com.open.pesantren.exception.DataNotFoundException
import com.open.pesantren.model.RestResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@RestControllerAdvice
class ExceptionController {

    @ExceptionHandler(value = [DataNotFoundException::class])
    fun runtimeErrorHandler(runtimeException: DataNotFoundException): RestResponse<String> {
        return RestResponse(
                code = 400,
                status = "BAD REQUEST",
                data = runtimeException.message!!
        )
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun validationHandler(constraintViolationException: ConstraintViolationException): RestResponse<String> {
        return RestResponse(
                code = 400,
                status = "BAD REQUEST",
                data = constraintViolationException.message!!
        )
    }

}