package core.craft.openingservice.feign;

import core.craft.openingservice.dto.RewardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "reward-service", url = "${REWARD_SERVICE_BASE_URL}")
public interface OpeningInterface {
    @GetMapping("/api/rewards/crate/{crateId}")
    public ResponseEntity<List<RewardDto>> listByCrate(@PathVariable Long crateId);

    @GetMapping("/api/rewards/{rewardId}")
    public ResponseEntity<RewardDto> get(@PathVariable Long rewardId);
}
