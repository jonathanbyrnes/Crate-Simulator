package core.craft.openingservice.service;

import core.craft.openingservice.domain.Opening;
import core.craft.openingservice.dto.OpeningDto;
import core.craft.openingservice.dto.OpeningSummaryDto;
import core.craft.openingservice.dto.RewardOpeningCountDto;
import core.craft.openingservice.dto.RewardDto;
import core.craft.openingservice.exception.ApprovedRewardNotFoundException;
import core.craft.openingservice.exception.CrateNotApprovedException;
import core.craft.openingservice.exception.RewardForCrateNotFoundException;
import core.craft.openingservice.exception.RewardNotFoundException;
import core.craft.openingservice.feign.OpeningInterface;
import core.craft.openingservice.repository.OpeningRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OpeningServiceImpl implements OpeningService {

    private final OpeningRepository repository;
    private final OpeningInterface openingInterface;

    @Override
    public OpeningDto open(Long crateId) {
        ResponseEntity<List<RewardDto>> choicesResponseEntity;
        try {
            choicesResponseEntity = openingInterface.listApprovedByCrate(crateId);
        } catch (FeignException.NotFound ex) {
            throw new RewardForCrateNotFoundException(crateId);
        } catch (FeignException.Conflict ex) {
            throw new CrateNotApprovedException(crateId);
        }
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

    @Override
    @Transactional(readOnly = true)
    public OpeningSummaryDto summarise(Long crateId) {
        Map<Long, String> rewardNames;
        try {
            List<RewardDto> rewards = openingInterface.listByCrate(crateId).getBody();
            rewardNames = rewards == null ? Map.of()
                    : rewards.stream().collect(Collectors.toMap(RewardDto::getId, RewardDto::getName));
        } catch (FeignException.NotFound ex) {
            throw new RewardForCrateNotFoundException(crateId);
        }

        List<RewardOpeningCountDto> counts = repository.countByRewardForCrate(crateId).stream()
                .map(c -> new RewardOpeningCountDto(c.getRewardId(), rewardNames.get(c.getRewardId()), c.getCount()))
                .toList();
        long total = counts.stream().mapToLong(RewardOpeningCountDto::getCount).sum();

        return new OpeningSummaryDto(crateId, total, counts);
    }

    private OpeningDto toDto(Opening opening) {
        ResponseEntity<RewardDto> rewardDto;
        try {
            rewardDto = openingInterface.get(opening.getRewardId());
        } catch (FeignException.NotFound ex) {
            throw new RewardNotFoundException(opening.getRewardId());
        }
        if(!rewardDto.hasBody()) {
            throw new RewardNotFoundException(opening.getRewardId());
        }

        assert rewardDto.getBody() != null;
        String rewardName = rewardDto.getBody().getName();

        return new OpeningDto(opening.getId(), opening.getCrateId(), opening.getRewardId(), rewardName, opening.getTimestamp());
    }

}
