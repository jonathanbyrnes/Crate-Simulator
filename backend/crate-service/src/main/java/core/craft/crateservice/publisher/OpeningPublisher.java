package core.craft.crateservice.publisher;

import core.craft.crateservice.dto.CreateOpeningRequest;

public interface OpeningPublisher {
    void publishOpening(CreateOpeningRequest event);
}
