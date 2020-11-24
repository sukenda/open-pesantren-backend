package com.open.pesantren.model

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
data class UserRequest(

        @NotBlank
        val username: String?,

        @NotBlank
        val password: String?,

        @NotBlank
        val email: String?,

        @NotBlank
        val profile: String?,

        @NotEmpty
        val roles: MutableSet<String>?
)
