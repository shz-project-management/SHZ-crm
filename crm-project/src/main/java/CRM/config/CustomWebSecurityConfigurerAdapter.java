package CRM.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("*").authenticated().and().httpBasic().and().csrf().disable();
//        http.addFilterAfter(new AuthorizationFilter(), BasicAuthenticationFilter.class);
//        http.addFilterAfter(new PermissionFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }
}