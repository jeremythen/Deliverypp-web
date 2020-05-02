package com.deliverypp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    
    @Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.httpBasic()
		.disable()
		.authorizeRequests()
        .antMatchers("/").permitAll()

		.antMatchers(GET, "/api/products").permitAll()
        .antMatchers("/api/products").hasRole("ADMIN")
        .antMatchers("/api/products/*").hasRole("ADMIN")
        .antMatchers(GET,"/api/orders").hasAnyRole("ADMIN")
        .antMatchers(DELETE,"/api/orders").hasAnyRole("ADMIN")
        .antMatchers(PUT,"/api/orders").hasAnyRole("ADMIN")
        .antMatchers("/api/orders/*").hasAnyRole("ADMIN")
        .antMatchers("/api/users").hasRole("ADMIN")
        .antMatchers(POST, "/api/register").permitAll()
        .antMatchers(POST, "/api/login").permitAll()
        .antMatchers(POST, "/api/auth/*").permitAll()
        .antMatchers(POST,"/api/orders").hasAnyRole("USER", "ADMIN")
        .antMatchers("/products").hasRole("ADMIN")
        .antMatchers("/users").hasRole("ADMIN")
        .antMatchers("/orders").hasAnyRole("ADMIN")
        .antMatchers("/locations").hasAnyRole("ADMIN")
        .antMatchers("/addresses").hasAnyRole("ADMIN")

        .antMatchers("/products/*").hasRole("ADMIN")
        .antMatchers("/users/*").hasRole("ADMIN")
        .antMatchers("/orders/*").hasAnyRole("ADMIN")
        .antMatchers("/locations/*").hasAnyRole("ADMIN")
        .antMatchers("/addresses/*").hasAnyRole("ADMIN")

		//.anyRequest()
		//.authenticated()
		.and()
		.cors()
		.and()
		.csrf()
		.disable()
		.exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}