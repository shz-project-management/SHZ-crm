package CRM.config;

import CRM.utils.GithubCodeDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class GithubConfig {

    @Value("${github.client_id}")
    private String clientId;
    @Value("${github.client_secret}")
    private String clientSecret;
    @Value("${github.access_token_url}")
    private String accessTokenUrl;
    @Value("${github.redirect_url}")
    private String redirectUrl;

    @Bean
    public GithubCodeDecoder githubCodeDecoder() {
        return new GithubCodeDecoder(clientId, clientSecret, accessTokenUrl, redirectUrl);
    }
}