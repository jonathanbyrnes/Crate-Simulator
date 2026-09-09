package core.craft.openingservice.config;

import core.craft.openingservice.service.Randomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class RandomConfig {

    @Bean
    public Randomiser randomiser() {
        return bound -> ThreadLocalRandom.current().nextDouble(bound);
    }
}
