package core.craft.rewardservice.service;

import core.craft.rewardservice.dto.CreateRewardRequest;
import core.craft.rewardservice.dto.RewardDto;
import core.craft.rewardservice.dto.UpdateRewardRequest;

import java.util.List;

public interface RewardService {
    RewardDto create(CreateRewardRequest request);
    RewardDto update(Long rewardId, UpdateRewardRequest request);
    RewardDto findById(Long rewardId);
    List<RewardDto> findByCrateId(Long crateId);
    void delete(Long rewardId);
    RewardDto approve(Long rewardId);
    RewardDto disapprove(Long rewardId);
}
