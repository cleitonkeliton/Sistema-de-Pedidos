package com.order.system.security.config;

import com.order.system.security.jwt.JwtAuthenticationFilter;
import com.order.system.security.jwt.JwtAuthorizationFilter;
import com.order.system.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    // Configuração global de segurança
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()  // Habilita CORS
                .csrf().disable()  // Desabilita CSRF (APIs stateless não precisam)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Sem sessão
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()  // Endpoints públicos
                .antMatchers("/swagger-ui/**", "/api-docs/**").permitAll()  // Acesso ao Swagger
                .anyRequest().authenticated()  // Demais endpoints requerem autenticação
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider))  // Filtro de login
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), JwtAuthenticationFilter.class);  // Filtro de autorização
    }

    // Configuração do CORS (permitir frontend)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));  // Frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));  // Expor header do token

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // Configuração do AuthenticationManager (para login)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    // Expor o AuthenticationManager como Bean (usado no filtro de login)
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Bean para codificação de senhas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}