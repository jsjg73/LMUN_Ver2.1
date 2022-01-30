package com.jsjg73.lmun.security;

import com.jsjg73.lmun.jwt.JwtConfig;
import com.jsjg73.lmun.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.jsjg73.lmun.jwt.JwtUtil;
import com.jsjg73.lmun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private JwtConfig jwtConfig;
	private JwtUtil jwtUtil;
	private PasswordEncoder passwordEncoder;
	private UserService userService;

	@Autowired
	public SecurityConfig(JwtConfig jwtConfig, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserService userService) {
		this.jwtConfig = jwtConfig;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.addFilter(jwtAuthenticationFilter())
			.authorizeRequests()
			.antMatchers("/","/user","/user/login").permitAll()
			.antMatchers(HttpMethod.GET, "/meeting/{meetingId}").permitAll()
			.anyRequest().authenticated();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.authenticationProvider(daoAuthenticationProvider());
	}
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(){
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(userService);
		return provider;
	}

	private JwtUsernameAndPasswordAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtUsernameAndPasswordAuthenticationFilter filter =
				new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtUtil, jwtConfig);
		filter.setFilterProcessesUrl("/user/login");
		return filter;
	}
}
