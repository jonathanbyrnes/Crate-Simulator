package core.craft.openingservice.service;

import core.craft.openingservice.domain.Opening;
import core.craft.openingservice.dto.OpeningDto;
import core.craft.openingservice.dto.RewardDto;
import core.craft.openingservice.exception.ApprovedRewardNotFoundException;
import core.craft.openingservice.exception.RewardForCrateNotFoundException;
import core.craft.openingservice.exception.RewardNotFoundException;
import core.craft.openingservice.feign.OpeningInterface;
import core.craft.openingservice.repository.OpeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class OpeningServiceImpl implements OpeningService {

    private final OpeningRepository repository;
    private final OpeningInterface openingInterface;

    @Override
    public OpeningDto open(Long crateId) {
        ResponseEntity<List<RewardDto>> choicesResponseEntity = openingInterface.listByCrate(crateId);
        if(!choicesResponseEntity.hasBody()) {
            throw new RewardForCrateNotFoundException(crateId);
        }

        List<RewardDto> choices = choicesResponseEntity.getBody();
        assert choices != null;
        if(choices.isEmpty()) {
            throw new ApprovedRewardNotFoundException(crateId);
        }

        double total = choices.stream().mapToDouble(RewardDto::getWeight).sum();
        double pick = ThreadLocalRandom.current().nextDouble(total);
        RewardDto selected = null;
        for (RewardDto r : choices) {
            pick -= r.getWeight();
            if (pick <= 0) {
                selected = r;
                break;
            }
        }

        Opening opening = new Opening();
        opening.setCrateId(crateId);
        assert selected != null;
        opening.setRewardId(selected.getId());
        opening.setTimestamp(Instant.now());
        repository.save(opening);

        return toDto(opening);
    }

    private OpeningDto toDto(Opening opening) {
        ResponseEntity<RewardDto> rewardDto = openingInterface.get(opening.getRewardId());
        if(!rewardDto.hasBody()) {
            throw new RewardNotFoundException(opening.getRewardId());
        }

        assert rewardDto.getBody() != null;
        String rewardName = rewardDto.getBody().getName();

        return new OpeningDto(opening.getId(), opening.getCrateId(), opening.getRewardId(), rewardName, opening.getTimestamp());
    }

}
