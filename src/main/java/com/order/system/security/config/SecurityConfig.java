package com.order.system.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/*Configuração de segurança para a aplicação.*/
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /*
     Define o gerenciador de autenticação.
     @param authenticationConfiguration a configuração de autenticação.
     @return o gerenciador de autenticação.
     @throws Exception se ocorrer um erro ao criar o gerenciador de autenticação.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
     Configura a cadeia de filtros de segurança.
     @param http o objeto HttpSecurity para configuração.
     @return a cadeia de filtros de segurança.
     @throws Exception se ocorrer um erro na configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Proteção contra CSRF desabilitada para APIs REST (habilite conforme necessário)
                .csrf(csrf -> csrf.disable())

                // 2. Política de criação de sessão definida como STATELESS para APIs REST
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configuração de autorização de requisições
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/public/**").permitAll()
                        // Endpoints protegidos
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        // Qualquer outra requisição deve ser autenticada
                        .anyRequest().authenticated()
                )

                // 4. Configuração de login HTTP Basic
                .httpBasic();

        return http.build();
    }

    /*Define o serviço de detalhes do usuário em memória.
     @return o serviço de detalhes do usuário.*/
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
    /*Define o codificador de senhas.
     @return o codificador de senhas.*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
