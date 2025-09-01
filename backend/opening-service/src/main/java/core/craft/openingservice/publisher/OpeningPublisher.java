package core.craft.openingservice.publisher;

import core.craft.openingservice.dto.CreateOpeningRequest;

public interface OpeningPublisher {
    void publishOpening(CreateOpeningRequest event);
}
