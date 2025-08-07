package core.craft.crateservice.service;

import core.craft.crateservice.dto.CreateOpeningRequest;
import core.craft.crateservice.publisher.OpeningPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService{

    private final OpeningPublisher publisher;

    @Override
    public void simulate(Long crateId, int count) {
        for(int i=0; i<count; i++) {
            publisher.publishOpening(new CreateOpeningRequest(crateId));
        }
    }
}
