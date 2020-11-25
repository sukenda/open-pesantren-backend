package com.open.pesantren.model

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
data class UserResponse(

        val id: String?,

        val username: String,

        val email: String,

        val profile: String,

        val roles: Set<String>?,

        val active: Boolean = false,

        val accessToken: String? = null

)
