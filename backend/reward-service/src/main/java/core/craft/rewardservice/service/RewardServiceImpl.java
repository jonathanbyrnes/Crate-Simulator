package core.craft.rewardservice.service;

import core.craft.rewardservice.domain.Reward;
import core.craft.rewardservice.dto.*;
import core.craft.rewardservice.exception.CrateNotFoundException;
import core.craft.rewardservice.exception.RewardNotFoundException;
import core.craft.rewardservice.feign.RewardInterface;
import core.craft.rewardservice.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository repository;
    private final RewardInterface rewardInterface;

    @Override
    public RewardDto create(CreateRewardRequest request) {
        return saveAndReturnDto(new Reward(), request);
    }

    @Override
    public RewardDto update(Long rewardId, UpdateRewardRequest request) {
        Reward searchedForReward = getRewardById(rewardId);
        return saveAndReturnDto(searchedForReward, request);
    }

    @Override
    @Transactional(readOnly = true)
    public RewardDto findById(Long rewardId) {
        return repository.findById(rewardId)
                .map(this::toDto)
                .orElseThrow(() -> new RewardNotFoundException(rewardId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RewardDto> findByCrateId(Long crateId) {
        ResponseEntity<CrateDto> crateDtoResponseEntity = rewardInterface.get(crateId);
        if(!crateDtoResponseEntity.getStatusCode().is2xxSuccessful()
                || !crateDtoResponseEntity.hasBody()) {
            throw new CrateNotFoundException(crateId);
        }

        return repository.findByCrateId(crateId).stream().map(this::toDto).toList();
    }

    @Override
    public void delete(Long rewardId) {
        if (!repository.existsById(rewardId)) {
            throw new RewardNotFoundException(rewardId);
        }
        repository.deleteById(rewardId);
    }

    @Override
    public RewardDto approve(Long rewardId) {
        return setApproval(rewardId, true);
    }

    @Override
    public RewardDto disapprove(Long rewardId) {
        return setApproval(rewardId, false);
    }

    private Reward getRewardById(Long rewardId) {
        return repository.findById(rewardId)
                .orElseThrow(() -> new RewardNotFoundException(rewardId));
    }

    private RewardDto toDto(Reward reward) {
        return new RewardDto(reward.getId(), reward.getCrateId(), reward.getName(), reward.getDescription(), reward.getWeight(), reward.isApproved());
    }

    private void mapRequestToEntity(CreateRewardRequest request, Reward reward) {
        ResponseEntity<CrateDto> crateDtoResponseEntity = rewardInterface.get(request.getCrateId());
        if(!crateDtoResponseEntity.getStatusCode().is2xxSuccessful()
                || !crateDtoResponseEntity.hasBody()) {
            throw new CrateNotFoundException(request.getCrateId());
        }

        reward.setCrateId(request.getCrateId());
        reward.setName(request.getName());
        reward.setDescription(request.getDescription());
        reward.setWeight(request.getWeight());
    }

    private void mapRequestToEntity(UpdateRewardRequest request, Reward reward) {
        reward.setName(request.getName());
        reward.setDescription(request.getDescription());
        reward.setWeight(request.getWeight());
    }

    private RewardDto saveAndReturnDto(Reward reward, CreateRewardRequest request) {
        mapRequestToEntity(request, reward);
        return toDto(repository.save(reward));
    }

    private RewardDto saveAndReturnDto(Reward reward, UpdateRewardRequest request) {
        mapRequestToEntity(request, reward);
        return toDto(repository.save(reward));
    }

    private RewardDto setApproval(Long rewardId, boolean approved) {
        Reward searchedForReward = getRewardById(rewardId);
        searchedForReward.setApproved(approved);
        return toDto(repository.save(searchedForReward));
    }


}
