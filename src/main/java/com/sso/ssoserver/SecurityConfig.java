package com.sso.ssoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private CustAuthenticationProvider custProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .authorizeRequests().antMatchers("/login","/check/token", "/oauth/**")
//                .permitAll()
                .requestMatchers()
//                .antMatchers("/login", "/check/token", "/static/**", "/oauth/**","http://localhost:8081/login")
                .antMatchers("/login", "/authorize","/check/token", "/static/**", "/oauth/**")
                .and()
                .authorizeRequests()
                .antMatchers("/admin/test").hasRole("ADMIN")
                .antMatchers("/user/test").hasRole("USER")
                .antMatchers("/check/token").permitAll()
                .antMatchers("/login", "/authorize").permitAll()
//                .antMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // 修改为vue的登录页面
//                .formLogin().loginPage("http://localhost:8081/login").permitAll();
                // 修改为本地自定义登录页面
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/authorize")
                .permitAll()
                // 默认login画面
//                .formLogin().permitAll();
//                .and()
//                .cors()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.parentAuthenticationManager(authenticationManager)
        auth
//                .inMemoryAuthentication()
//                .withUser("test").password(passwordEncoder().encode("123")).roles("USER")
//                .and()
//                .withUser("admin").password(passwordEncoder().encode("123")).roles("ADMIN");
                .authenticationProvider(custProvider);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}