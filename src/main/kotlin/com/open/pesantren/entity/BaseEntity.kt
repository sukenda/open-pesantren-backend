package com.open.pesantren.entity

import org.springframework.data.annotation.*
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/

@Document
data class BaseEntity(

        @Id
        private val id: String? = null,

        @CreatedBy
        private val createdBy: String? = null,

        @CreatedDate
        private val createdDate: LocalDateTime? = null,

        @LastModifiedBy
        private val updatedBy: String? = null,

        @LastModifiedDate
        private val updatedDate: LocalDateTime? = null,

        @Version
        private val version: Int? = null
)
