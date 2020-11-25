package com.open.pesantren.controller

import com.open.pesantren.exception.DataNotFoundException
import com.open.pesantren.exception.InvalidUserPasswordException
import com.open.pesantren.exception.UserExistException
import com.open.pesantren.model.RestResponse
import org.springframework.http.HttpStatus
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
                code = HttpStatus.NOT_FOUND.value(),
                status = HttpStatus.NOT_FOUND.name,
                data = runtimeException.message!!
        )
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun validationHandler(constraintViolationException: ConstraintViolationException): RestResponse<String> {
        return RestResponse(
                code = HttpStatus.BAD_REQUEST.value(),
                status = HttpStatus.BAD_REQUEST.name,
                data = constraintViolationException.message!!
        )
    }

    @ExceptionHandler(value = [UserExistException::class])
    fun validationHandler(userExistException: UserExistException): RestResponse<String> {
        return RestResponse(
                code = HttpStatus.FOUND.value(),
                status = HttpStatus.FOUND.name,
                data = userExistException.message!!
        )
    }

    @ExceptionHandler(value = [InvalidUserPasswordException::class])
    fun validationHandler(invalidUserPasswordException: InvalidUserPasswordException): RestResponse<String> {
        return RestResponse(
                code = HttpStatus.FORBIDDEN.value(),
                status = HttpStatus.FORBIDDEN.name,
                data = invalidUserPasswordException.message!!
        )
    }

}