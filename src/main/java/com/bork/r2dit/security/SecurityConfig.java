package com.bork.r2dit.security;

import com.bork.r2dit.entity.R2User;
import com.bork.r2dit.repository.R2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

    @Autowired
    private R2UserRepository R2UserRepository;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;


    private static SecurityConfig securityConfig;
    public static SecurityConfig getInstance() {
        if (securityConfig == null) {
            securityConfig = new SecurityConfig();
        }

        return securityConfig;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/", "signup", "/styles.css", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .formLogin(loginPage -> loginPage
                        .loginPage("/login")
                        .permitAll()
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
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
                .usersByUsernameQuery("select username,password,enabled from users where username = ?")
                .authoritiesByUsernameQuery("select username,authority from authorities where username = ?")
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }


    public void createUser(String username, String password) {
        final var userDetailsManager = userDetailsManager(dataSource);

        //Previous matching user will be deleted
        final var user = User.builder().username(username).password(passwordEncoder.encode(password)).roles("R2User").build();
        userDetailsManager.deleteUser(username);
        R2UserRepository.findByUsername(username).ifPresent(R2User -> R2UserRepository.deleteById(R2User.getId()));
        userDetailsManager.createUser(user);
        R2UserRepository.save(new R2User(username, passwordEncoder.encode(password)));
    }

//    @Bean
//    public CommandLineRunner commandLineRunner() {
//        return args -> {
//            createUser("admin", "admin");
//        };
//    }
}