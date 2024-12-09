package spring.alotra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {
    @Bean
    public CurrencyFormatter currencyFormatter() {
        return new CurrencyFormatter();
    }
}
