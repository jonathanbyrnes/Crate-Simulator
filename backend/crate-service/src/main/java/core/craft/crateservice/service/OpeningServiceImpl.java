package core.craft.crateservice.service;

import core.craft.crateservice.domain.Crate;
import core.craft.crateservice.domain.Opening;
import core.craft.crateservice.domain.Reward;
import core.craft.crateservice.dto.OpeningDto;
import core.craft.crateservice.exception.CrateNotFoundException;
import core.craft.crateservice.repository.CrateRepository;
import core.craft.crateservice.repository.OpeningRepository;
import core.craft.crateservice.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
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
    private final CrateRepository crateRepo;
    private final RewardRepository rewardRepo;

    @Override
    public OpeningDto open(Long crateId) {
        Crate crate = crateRepo.findById(crateId)
                .orElseThrow(()->new CrateNotFoundException(crateId));

        List<Reward> choices = rewardRepo.findByCrateId(crateId).stream()
                .filter(Reward::isApproved)
                .toList();
        if (choices.isEmpty()) {
            throw new NoSuchElementException("No approved rewards in crate " + crateId);
        }

        double total = choices.stream().mapToDouble(Reward::getWeight).sum();
        double pick = ThreadLocalRandom.current().nextDouble(total);
        Reward selected = null;
        for (Reward r : choices) {
            pick -= r.getWeight();
            if (pick <= 0) {
                selected = r;
                break;
            }
        }

        Opening opening = new Opening();
        opening.setCrate(crate);
        opening.setReward(selected);
        opening.setTimestamp(Instant.now());
        repository.save(opening);

        return toDto(opening);
    }

    private OpeningDto toDto(Opening opening) {
        return new OpeningDto(opening.getId(), opening.getCrate().getId(), opening.getReward().getId(), opening.getReward().getName(), opening.getTimestamp());
    }

}
