package org.snowshoe.community.data.model

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    USER {
        override fun getAuthority(): String {
            return "USER"
        }
    }
}
