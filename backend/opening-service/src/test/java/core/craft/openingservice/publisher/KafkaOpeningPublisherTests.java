package core.craft.openingservice.publisher;

import core.craft.openingservice.dto.CreateOpeningRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaOpeningPublisherTests {

    @Mock
    private KafkaTemplate<String, CreateOpeningRequest> kafka;

    @InjectMocks
    private KafkaOpeningPublisher publisher;

    @Test
    public void publishOpening() {
        CreateOpeningRequest event = new CreateOpeningRequest(42L);

        publisher.publishOpening(event);

        verify(kafka).send("crate.open.request", event);
    }
}
