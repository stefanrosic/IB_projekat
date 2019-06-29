package com.bezbednost.configuration;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.bezbednost.security.AuthenticationEntryLocation;
import com.bezbednost.security.TokenAuthenticationFilter;
import com.bezbednost.security.TokenHelper;
import com.bezbednost.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AuthenticationEntryLocation authenticationEntryLocation;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenHelper tokenHelper;
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception{
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/authentication").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/authentication/e").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/user/createAcc").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/upload").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/keystore/generate").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryLocation);
        http.addFilterBefore(new TokenAuthenticationFilter(tokenHelper, userService), BasicAuthenticationFilter.class);

		http.csrf().disable();
    }
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		try {			
			auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {   
        web.ignoring().antMatchers(
                HttpMethod.POST,
                "/authentication",
                "user/createAcc"
                
        );
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/",
                "/authentication/e",
                "/webjars/**",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
                
            );
	}
}
