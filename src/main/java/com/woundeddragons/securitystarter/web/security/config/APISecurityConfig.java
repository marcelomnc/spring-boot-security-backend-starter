package com.woundeddragons.securitystarter.web.security.config;

import com.woundeddragons.securitystarter.business.security.service.CustomUserDetailsService;
import com.woundeddragons.securitystarter.web.security.api.v1.Constants;
import com.woundeddragons.securitystarter.web.security.api.v1.controller.AuthenticationController;
import com.woundeddragons.securitystarter.web.security.api.v1.controller.UserSignUpController;
import com.woundeddragons.securitystarter.web.security.filter.JWTProcessorFilter;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JWTProcessorFilter jwtProcessorFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.customUserDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .disable()
                .antMatcher(Constants.API_VERSION_PATH + "/**")
                .authorizeRequests()
                .antMatchers(com.woundeddragons.securitystarter.web.security.api.v1.Constants.API_VERSION_PATH + AuthenticationController.PATH)
                .permitAll()
                .antMatchers(com.woundeddragons.securitystarter.web.security.api.v1.Constants.API_VERSION_PATH + UserSignUpController.PATH)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterBefore(this.jwtProcessorFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
