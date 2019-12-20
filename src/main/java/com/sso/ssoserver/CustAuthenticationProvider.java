package com.sso.ssoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsService userService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        if("123".equals(password)){
            UserDetails userDetials = (UserDetails) userService.loadUserByUsername(username);
            Collection<? extends GrantedAuthority> authorities = userDetials.getAuthorities();
            return new UsernamePasswordAuthenticationToken(userDetials, password, authorities);
        } else {
            return null;
        }

    }
    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}