package org.snowshoe.community.data.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

data class Profile(
    private val id: UUID,
    private val username: String,
    private val password: String,
    private val authorities: MutableList<Role>,
    private val isAccountExpired: Boolean,
    private val isAccountLocked: Boolean,
    private val isCredentialExpired: Boolean,
    private val isAccountEnabled: Boolean
) : UserDetails {
    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    fun getId(): UUID {
        return id
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun isAccountNonExpired(): Boolean {
        return isAccountExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return isAccountLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return !isCredentialExpired
    }

    override fun isEnabled(): Boolean {
        return isAccountEnabled
    }
}

