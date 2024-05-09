package com.bork.r2dit.security;

import com.bork.r2dit.entity.R2User;
import com.bork.r2dit.repository.R2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final R2UserRepository R2UserRepository;

    public SecurityConfig(R2UserRepository r2UserRepository) {
        R2UserRepository = r2UserRepository;
    }

    //Allow access to H2 console without authentication
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(antMatcher("/h2-console/**")).permitAll()
                                .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .formLogin(withDefaults())
                .build();
    }

    @Autowired
    public void configure(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            DataSource dataSource,
            PasswordEncoder passwordEncoder


    ) throws Exception {

        authenticationManagerBuilder
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
        final var userDetailsManager = new JdbcUserDetailsManager(dataSource);
        createUser("ivo", passwordEncoder, userDetailsManager);
        createUser("joe", passwordEncoder, userDetailsManager);
        return userDetailsManager;
    }

    protected void createUser(String username, PasswordEncoder passwordEncoder, JdbcUserDetailsManager userDetailsManager) {
        final var user = User.builder().username(username).password(passwordEncoder.encode("admin")).roles("R2User").build();
//        userDetailsManager.deleteUser(username);
        R2UserRepository.findByUsername(username).ifPresent(R2User -> R2UserRepository.deleteById(R2User.getId()));
        userDetailsManager.createUser(user);
        R2UserRepository.save(new R2User(username, passwordEncoder.encode("admin")));
    }

}