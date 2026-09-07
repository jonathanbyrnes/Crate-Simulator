package core.craft.openingservice.controller;

import core.craft.openingservice.service.OpeningRequestService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crates/{crateId}/simulate")
@RequiredArgsConstructor
public class SimulationController {

    private final OpeningRequestService service;

    public static final int MAX_COUNT = 10_000;

    @PostMapping
    public ResponseEntity<Void> simulate(@PathVariable Long crateId,
                                         @RequestParam(defaultValue = "1000") @Min(1) @Max(MAX_COUNT) int count) {
        service.requestOpenings(crateId, count);
        return ResponseEntity.accepted().build();
    }
}
