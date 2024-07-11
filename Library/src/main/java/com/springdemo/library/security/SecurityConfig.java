package com.springdemo.library.security;

import com.springdemo.library.security.filters.JwtAuthenticationFilter;
import com.springdemo.library.security.filters.OtpAuthenticationFilter;
import com.springdemo.library.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    @Bean
    public OtpAuthenticationFilter otpAuthenticationFilter() {
        return new OtpAuthenticationFilter();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login", "/processlogin", "/sendotp",
                                        "/signup", "/processsignup", "/auth", "/changepassword",
                                        "/forgotpassword", "/processforgotpassword",
                                        "/isvalidemail", "/isvalidsodienthoai", "/gallery", "/slogan",
                                        "/isvalidsocccd", "/isvalidtenuser", "/aboutus", "/rule",
                                        "/home", "/book/**", "/blog/**").permitAll()
                                .requestMatchers("/management/login", "/management/processlogin").permitAll()
                                .requestMatchers("/management/staff/**", "/management/customers/**").hasRole("ADMIN")
                                .requestMatchers("/management/manageBookBorrowed/**", "/Library/management/manageBaiVietCanDuyet/**").hasRole("STAFF")
                                .requestMatchers("/management/**").hasAnyRole("ADMIN", "STAFF")
                                //.requestMatchers("/cart/**").permitAll()
                                //.requestMatchers("/").hasRole("ROLE_CUSTOMER")
                                //.requestMatchers("/").hasRole("ROLE_COLLABORATOR")
                                .anyRequest().authenticated()
                ).logout(logout -> logout
                        .logoutUrl("/Library/logout").permitAll()
                        .deleteCookies("JSESSIONID", Constants.JWT_COOKIE_NAME)
                        .clearAuthentication(true)
                );
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(otpAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**",
                "/static-admin_and_staff/**", "/tinymce/**", "/select2/**");
    }
}
