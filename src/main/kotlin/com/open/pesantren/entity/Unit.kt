package com.open.pesantren.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by Kenda on 02 Dec 2020
 * Email soekenda09@gmail.com
 **/
@Document(collection = "units")
data class Unit(

    @Id
    private val id: String? = null,

    private val nama: String,

    )
