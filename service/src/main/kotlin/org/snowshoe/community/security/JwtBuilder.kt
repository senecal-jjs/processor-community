package org.snowshoe.community.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.snowshoe.community.config.JWTProperties
import org.snowshoe.community.data.model.Profile
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
@EnableConfigurationProperties(JWTProperties::class)
class JwtBuilder(val jwtProperties: JWTProperties) {
    private val logger = LoggerFactory.getLogger(JwtBuilder::class.java)
    private val secretKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    fun generateToken(authentication: Authentication): String {
        val userPrincipal: Profile = authentication.principal as Profile
        val expiryDate = Date(Date().time + (jwtProperties.expiresInMinutes * 60 * 1000))

        return Jwts.builder()
            .setSubject(userPrincipal.getId().toString())
            .claim("USERNAME", userPrincipal.username)
            .claim("ROLES", userPrincipal.authorities)
            .setIssuedAt(Date())
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getUserIdFromToken(token: String): UUID {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .let {
                UUID.fromString(it.subject)
            }
    }

    fun isTokenValid(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)

            return true
        } catch (e: SecurityException) {
            logger.error("Invalid JWT signature");
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token");
        } catch (e: ExpiredJwtException) {
            logger.error("Expired JWT token");
        } catch (e: UnsupportedJwtException) {
            logger.error("Unsupported JWT token");
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty.");
        }
        return false
    }
}