package com.immoben.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService () {
		return new ImmobenUserDetailsService();
	}


	@Bean
	public PasswordEncoder passwordEncoder () {
		return new BCryptPasswordEncoder();
	}


	public DaoAuthenticationProvider authenticationProvider () {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}


	@Override
	protected void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}


	@Override
	protected void configure (HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin").antMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson").antMatchers("/categories/**", "/cities/**").hasAnyAuthority("Admin", "Editor")

				.antMatchers("/products/new", "/products/delete/**", "/products/edit/**").hasAnyAuthority("Admin", "Editor")

				.antMatchers("/products/edit/**", "/products/save", "/products/check_unique").hasAnyAuthority("Admin", "Salesperson")

				.antMatchers("/products", "/products/new", "/products/detail/**", "/products/page/**").hasAnyAuthority("Admin", "Salesperson", "Shipper", "Editor")

				.antMatchers("/products/**").hasAnyAuthority("Admin", "Editor")

				.antMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")

				.antMatchers("/customers/**", "/orders/**", "/get_shipping_cost").hasAnyAuthority("Admin", "Salesperson")

				.antMatchers("/orders_shipper/update/**").hasAnyAuthority("Shipper")

				.antMatchers("/products", "/products/new", "/products/page/**", "/products/edit/**", "/products/save", "/products/check_unique", "/products/detail")

				.hasAnyAuthority("Admin", "Assistant", "Editor")

				.anyRequest().authenticated().and().formLogin().loginPage("/login").usernameParameter("email").permitAll().and().logout().permitAll().and().rememberMe().key("AbcDefgHijKlmnOpqrs_1234567890").tokenValiditySeconds(24 * 14 * 24 * 60 * 60);

		http.headers().frameOptions().sameOrigin();
	}


	@Override
	public void configure (WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		return new CustomerUserDetailsService();
//	}
//	
//	@Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//
//		return authProvider;
//	}
//
//	
}
