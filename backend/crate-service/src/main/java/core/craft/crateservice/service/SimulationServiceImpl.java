package core.craft.crateservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService{

    private final OpeningRequestService service;

    @Override
    public void simulate(Long crateId, int count) {
        service.requestOpenings(crateId, count);
    }
}
