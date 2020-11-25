package com.open.pesantren.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

/**
 * Created by Kenda on 24 Nov 2020
 * Email soekenda09@gmail.com
 **/
@Document(collection = "users")
data class User(

        @Id
        val id: String,

        @Indexed(unique = true)
        var email: String,

        @Indexed(unique = true)
        var name: String,

        var pass: String,

        var enabled: Boolean,

        var profile: String,

        var roles: Set<String>,

        var refreshToken: String,

        ) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.stream().map { role: String -> SimpleGrantedAuthority(role) }.collect(Collectors.toList())
    }

    override fun getPassword(): String {
        return pass
    }

    override fun getUsername(): String {
        return name
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}