package com.motorro.cookbook.server

import io.ktor.server.application.*
import io.ktor.server.auth.*
import ru.otus.arch.server.PASSWORD
import ru.otus.arch.server.USERNAME
import ru.otus.arch.Constants as CommonConstants

/**
 * User principal
 */
data class UserNamePrincipal(val username: String)

/**
 * User access
 */
interface Auth {
    /**
     * Authentication
     */
    fun auth(authenticationConfig: AuthenticationConfig, area: String)

    class Impl() : Auth {
        override fun auth(authenticationConfig: AuthenticationConfig, area: String) {
            authenticationConfig.basic(area) {
                this.realm = CommonConstants.Auth.REALM_ADMIN
                this.validate { credentials ->

                    if (USERNAME == credentials.name && PASSWORD == credentials.password) {
                        this.application.log.info("User ${credentials.name} authenticated")
                        UserNamePrincipal(credentials.name)
                    } else {
                        this.application.log.warn("User ${credentials.name} authentication failed")
                        null
                    }
                }
            }
        }
    }
}