package core.craft.crateservice.service;

import core.craft.crateservice.dto.CreateOpeningRequest;
import core.craft.crateservice.publisher.OpeningPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpeningRequestServiceImpl implements OpeningRequestService {

    private final OpeningPublisher publisher;

    @Override
    public void requestOpening(Long crateId) {
        publisher.publishOpening(new CreateOpeningRequest(crateId));
    }

    @Override
    public void requestOpenings(Long crateId, int count) {
        for (int i = 0; i < count; i++) requestOpening(crateId);
    }
}
