package com.sso.ssoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ClientDetailsService customClientDetailsService;

    @Qualifier("customUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
//    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
//                .checkTokenAccess("isAuthenticated()")
                // 放开默认的/oauth/check_token接口的访问限制
                .checkTokenAccess("permitAll()")
//                .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                .allowFormAuthenticationForClients();
    }

    @Bean
    public TokenStore tokenStore() {
        MyRedisTokenStore redis = new MyRedisTokenStore(connectionFactory);
        return redis;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String[] redirectUris = {"http://localhost:8082/ui/hehe", "http://localhost:8082/ui/login","http://www.baidu.com"};
        clients.inMemory()
                .withClient("SampleClientId")
                .secret(passwordEncoder().encode("secret"))
                .authorizedGrantTypes("authorization_code")
                // 20191217 如果需要显示授权页面，scope不能设为all，不然就算autoApprove(false)也不能显示授权页面
                .scopes("read")
//                .autoApprove(true)
                .autoApprove(false)
                .redirectUris(redirectUris)

                .and()

                .withClient("111")
                .secret(passwordEncoder().encode("222"))
                .authorizedGrantTypes("authorization_code","refresh_token")
                .scopes("all")
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(60)
//                .autoApprove(true)
                .redirectUris(redirectUris)

                .and()

                .withClient("222")
                .secret(passwordEncoder().encode("222"))
                .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                .authorities("ROLE_USER","ROLE_ADMIN")
                .scopes("all")
                .accessTokenValiditySeconds(72000)
                .refreshTokenValiditySeconds(3600000)
//                .autoApprove(true)
                .redirectUris(redirectUris);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)// add get method
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService);
        // 最后一个参数为替换之后授权页面的url
        endpoints.pathMapping("/oauth/confirm_access","/custom/confirm_access");

    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
