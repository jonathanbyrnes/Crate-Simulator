package core.craft.crateservice.listener;

import core.craft.crateservice.dto.CreateOpeningRequest;
import core.craft.crateservice.service.OpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpeningListener {

    private final OpeningService openingService;

    @KafkaListener(topics = "crate.open.request", groupId = "crate-service")
    public void onOpeningRequested(CreateOpeningRequest request) {
        openingService.open(request.getCrateId());
    }
}
