package CRM.config;

import CRM.filter.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter {

    @Autowired
    private AuthorizationFilter authFilter;
//    @Autowired
//    private PermissionFilter permissionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/").permitAll().antMatchers("*").authenticated().and().httpBasic().and().csrf().disable().cors();
        http.addFilterAfter(authFilter, BasicAuthenticationFilter.class);
//        http.addFilterAfter(permissionFilter, AuthorizationFilter.class);
        return http.build();
    }
}