package com.open.pesantren.model

import javax.validation.constraints.NotBlank

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
data class RefreshTokenRequest(

        @NotBlank
        val refreshToken: String,

        )
