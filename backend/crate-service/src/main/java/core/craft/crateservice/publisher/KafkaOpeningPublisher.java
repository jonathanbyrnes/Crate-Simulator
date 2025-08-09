package core.craft.crateservice.publisher;

import core.craft.crateservice.dto.CreateOpeningRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaOpeningPublisher implements OpeningPublisher {

    private final KafkaTemplate<String, CreateOpeningRequest> kafka;

    @Override
    public void publishOpening(CreateOpeningRequest event) {
        kafka.send("crate.open.request", event);
    }
}
