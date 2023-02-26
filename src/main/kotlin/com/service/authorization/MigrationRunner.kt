package com.service.authorization

import com.service.authorization.user.UserService
import com.service.authorization.userRole.UserRoleService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class MigrationRunner(
        private val userService: UserService,
        private val userRoleService: UserRoleService
): ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val userDetails: UserDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build()
        if(userService.getBy(userDetails.username) != null) {
            return
        }
        val user = userService.save(userDetails)
        userRoleService.saveAll(user.id,  userDetails.authorities.toList())
    }
}