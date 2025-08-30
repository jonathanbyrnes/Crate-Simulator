package core.craft.rewardservice.feign;

import core.craft.rewardservice.dto.CrateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crate-service", url = "${CRATE_SERVICE_BASE_URL}")
public interface RewardInterface {
    @GetMapping("/api/crates/{crateId}")
    public ResponseEntity<CrateDto> get(@PathVariable Long crateId);
}
