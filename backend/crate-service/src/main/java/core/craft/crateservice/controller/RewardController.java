package core.craft.crateservice.controller;

import core.craft.crateservice.dto.CreateRewardRequest;
import core.craft.crateservice.dto.RewardDto;
import core.craft.crateservice.dto.UpdateRewardRequest;
import core.craft.crateservice.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService service;

    @PostMapping
    public ResponseEntity<RewardDto> create(@Validated @RequestBody CreateRewardRequest request) {
        RewardDto rewardDto = service.create(request);
        return ResponseEntity.created(URI.create("/api/rewards" + rewardDto.getId()))
                .body(rewardDto);
    }

    @GetMapping("/crate/{crateId}")
    public ResponseEntity<List<RewardDto>> listByCrate(@PathVariable Long crateId) {
        return ResponseEntity.ok(service.findByCrateId(crateId));
    }

    @GetMapping("/{rewardId}")
    public ResponseEntity<RewardDto> get(@PathVariable Long rewardId) {
        return ResponseEntity.ok(service.findById(rewardId));
    }

    @PutMapping("/{rewardId}")
    public ResponseEntity<RewardDto> update(@PathVariable Long rewardId,
                                           @RequestBody UpdateRewardRequest request) {
        return ResponseEntity.ok(service.update(rewardId, request));
    }

    @DeleteMapping("/{rewardId}")
    public ResponseEntity<Void> delete(@PathVariable Long rewardId) {
        service.delete(rewardId);
        return ResponseEntity.noContent().build();
    }

}
