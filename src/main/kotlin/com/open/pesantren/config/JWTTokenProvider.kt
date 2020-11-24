package com.open.pesantren.config

import com.open.pesantren.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.Serializable
import java.security.Key
import java.util.*
import javax.annotation.PostConstruct
import kotlin.collections.HashMap

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/

@Component
class JWTTokenProvider : Serializable {

    private val created = "created"
    private val username = "username"
    private val roles = "roles"

    @Value("\${jwt.secret}")
    private var secretKey: String? = null

    @Value("\${jwt.expiration}")
    private val expirationTime: String? = null

    @Value("\${jwt.refresh.expiration}")
    private val expirationRefreshTime: String? = null

    private var key: Key? = null

    @PostConstruct
    protected fun init() {
        key = Keys.hmacShaKeyFor(secretKey!!.toByteArray())
        secretKey = Base64.getEncoder().encodeToString(secretKey!!.toByteArray())
    }

    fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
    }

    fun getUsernameFromToken(token: String): String? {
        return getAllClaimsFromToken(token).subject
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getAllClaimsFromToken(token).expiration
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun generateToken(param: User, roles: Set<String>, refresh: Boolean): String {
        val claims: MutableMap<String, Any?> = HashMap()
        claims[username] = param.username
        claims[this.roles] = roles
        claims[created] = Date()
        val expirationTimeLong = if (refresh) expirationRefreshTime!!.toLong() else expirationTime!!.toLong() //in second
        val createdDate = Date()
        val expirationDate = Date(createdDate.time + expirationTimeLong * 1000)
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(param.username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact()
    }

    fun validateToken(token: String): Boolean {
        return !isTokenExpired(token)
    }

}