package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

	    clients.inMemory()
	            .withClient("angular")
	            .secret("$2a$10$G1j5Rf8aEEiGc/AET9BA..xRR.qCpOUzBZoJd8ygbGy6tb3jsMT9G") // @ngul@r0 - Isto que deve ser digitado no password na aba Authorization do Postman. Lembrando que nesta aba deve ser colocado o Basic Auth no Type
	            .scopes("read", "write")
	            .authorizedGrantTypes("password", "refresh_token")
	            .accessTokenValiditySeconds(1800)
	            .refreshTokenValiditySeconds(3600 * 24)
				.and()
				.withClient("mobile")
				.secret("$2a$10$wciR8q9y8ByH4bffR4j0AOadZIyD8fUzoTa9aW6RvvF78O6eVftX.") // m0b1l30 - Isto que deve ser digitado no password na aba Authorization do Postman. Lembrando que nesta aba deve ser colocado o Basic Auth no Type
				.scopes("read")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(1800)
				.refreshTokenValiditySeconds(3600 * 24);
	}
	
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	    endpoints
	        .tokenStore(tokenStore())
	        .accessTokenConverter(this.accessTokenConverter())
	        .reuseRefreshTokens(false)
	        .userDetailsService(this.userDetailsService)
	        .authenticationManager(this.authenticationManager);
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks");
		return accessTokenConverter;
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
}
