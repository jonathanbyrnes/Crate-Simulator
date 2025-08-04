package core.craft.crateservice.service;

import core.craft.crateservice.dto.CreateRewardRequest;
import core.craft.crateservice.dto.RewardDto;
import core.craft.crateservice.dto.UpdateRewardRequest;

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
