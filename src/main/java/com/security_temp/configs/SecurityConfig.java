package com.security_temp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.security_temp.configs.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * AUTHENTICATION ARCHITECTURE
 * ------------------------------------------------------------------------------------
 *                                                                                    |
 * SecurityContextHolder is the heart of Authentication Architecture. It's used       |
 * for storing the details of the currently authenticated principal,                  |
 * contains SecurityContext, usually used to store the details of the currently       |
 * authenticated principal.                                                           |
 * SecurityContext contains Authentication object, an input to                        |
 * AuthenticationManager to determine who is authenticated, which contains the        |
 * principal,credentials, and authorities.                                            | 
 * Principal is the currently authenticated user - usually an instance of             |
 * UserDetails.                                                                       |
 * credentials are the password or token used to authenticate the user, it is         |
 * cleared after authentication.                                                      |
 * Authorities are the permissions granted to the principal. Two examples are         | 
 * Roles and Scopes.                                                                  |
 *                                                                                    |
 * SecurityContextHolder -> SecurityContext -> Authentication -> Principal,           | 
 * Credentials, Authorities                                                           |
 *                                                                                    |
 * AuthenticationManager is used to authenticate the an user. It                      |
 * has serveral implementations, the most common one is ProviderManager.              | 
 * ProviderManager is an implementation of AuthenticationManager that delegates       |
 * a list of AuthenticationProvider. It's used to authenticate the user by            |
 * delegating the authentication request to a list of AuthenticationProviders.        |
 *                                                                                    |
 * Authentication -> AuthenticationManager -> ProviderManager ->                      |
 * AuthenticationProviders                                                            |
 *                                                                                    |
 * There are many AuthenticationProviders, some of them are:                          |
 * - DaoAuthenticationProvider: It's used to authenticate the user using a            |
 * UserDetailsService.                                                                | 
 * - JwtAuthenticationProvider: It's used to authenticate the user using a JWT        |
 * token.                                                                             |
 * ...                                                                                |
 * ___________________________________________________________________________________|
 */
/**
 * AUTHORIZATION ARCHITECTURE
 * ------------------------------------------------------------------------------------
 * Before Spring Security 5, the authorization process was handled by the             |
 * AccessDecisionManager and AccessDecisionVoter.                                     |
 * Starting from Spring Security 5.5+, the authorization process is handled by        |
 * AuthorizationManager.                                                              |
 *                                                                                    |
 * AuthorizationManager = AccessDecisionManager + AccessDecisionVoter                 |                                                                     
 *                                                                                    |
 * After the user is authenticated, the next step is to authorize the user.           |
 * Authorization is the process of determining what the authenticated user is by using| 
 * GrantedAuthority - an interface that represents the authority granted to the       |
 * principal                                                                          |  
 * Easily Read by any AuthorizationManager implementation                             |
 * By default, role-based authorization rules include "ROLE_" as a prefix.            |
 * You can change prefix by using GrantedAuthorityDefaults                            |
 *                                                                                    |
 * There are many AuthorizationManagers, which responsible for making the decision    |
 * whether the user is authorized to access the resource or not.                      |
 * ___________________________________________________________________________________|
 */
/**
 * 
 * 
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationProvider authenticationProvider;

        /**
         * @param httpSecurity
         * @return
         * @throws Exception
         *                   - SecurityFilterChain is a filter chain that is responsible
         *                   for processing incoming requests and responses.
         *                   - SecurityFilterChain includes a series of filters, which
         *                   are excuted in specific order to perform authentication and
         *                   authorization.
         *                   - The filter chain is created by the HttpSecurity object,
         *                   which is a part of the Spring Security configuration.
         *                   - It's typically unnessary to know the order of the filters
         *                   in the chain, but it's important to know that the order is
         *                   like authentication filter will be excuted before
         *                   authorization filter.
         *                   - The HttpSecurity object provides a number of methods to
         *                   add filters to the filter chain.
         *                   - The addFilterBefore() method adds a filter before another
         *                   filter in the chain.
         *                   - The addFilterAfter() method adds a filter after another
         *                   filter in the chain.
         *                   - The addFilterAt() method adds a filter at a specific
         *                   position in the chain.
         *                   - We usaually use addFilterBefore() with the
         *                   UsernamePasswordAuthenticationFilter.class to add a custom
         *                   filter before the default authentication filter.
         *                   - authorizeHttpRequests() method is used to configure the
         *                   authorization rules for the incoming requests.
         *                   (AuthorizationFilter)
         *                   - Authorizing Request has many methods to configure the
         *                   authorization rules, such as: permitAll(), denyAll(),
         *                   hasRole(), hasAnyRole(), hasAuthority(), hasAnyAuthority(),
         *                   access(), authenticated(), requestMatchers(), anyRequest(),
         *                   ... etc.
         *                   - CAUTION: hasRole() and hasAnyRole() methods are used to
         *                   check if the user has a role with "ROLE_" prefix.
         *                   - CAUTION hasAuthority() and hasAnyAuthority() methods are
         *                   used to check if the user has a specific authority.
         *                   - Authorization Filter has @EnableMethodSecurity annotation
         *                   to enable method security. Associated with some common
         *                   annotations: @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
         *                   annotations.
         *                   - @PostAuthorize() can be used to ensures that the user can
         *                   only view their own user object after the method has
         *                   executed: @PostAuthorize("returnObject.username ==
         *                   authentication.name")
         *                   - PreFilter() can be used to filter the input list before
         *                   the method is executed: @PreFilter("filterObject.username
         *                   == authentication.name")
         * 
         */
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.disable())
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/", "/login").permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .authenticationProvider(authenticationProvider);

                return httpSecurity.build();
        }

}
