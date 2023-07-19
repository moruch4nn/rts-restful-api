package dev.mr3n.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*

fun Application.configureSecurity() {

    val jwtSecret = System.getenv("TOKEN_SECRET")
    authentication {
        jwt {
            verifier(JWT.require(Algorithm.HMAC256(jwtSecret)).build())
            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }
}
