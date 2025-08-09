package core.craft.crateservice.config;

import core.craft.crateservice.dto.CreateOpeningRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic openRequestTopic() {
        return TopicBuilder.name("crate.open.request")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public KafkaTemplate<String, CreateOpeningRequest> kafkaTemplate(
            ProducerFactory<String, CreateOpeningRequest> pf) {
        return new KafkaTemplate<>(pf);
    }
}
