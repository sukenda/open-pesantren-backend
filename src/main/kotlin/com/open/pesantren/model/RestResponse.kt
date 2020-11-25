package com.open.pesantren.model

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
data class RestResponse<T>(

        val status: String,

        val code: Int,

        val data: T,

        var rows: Int = 0
)