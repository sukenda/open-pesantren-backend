package com.open.pesantren.model

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
data class TokenResponse(

        var accessToken: String,

        val refreshToken: String,

        val type: String = "Bearer",
)
