package core.craft.crateservice.controller;

import core.craft.crateservice.service.OpeningRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crates/{crateId}/simulate")
@RequiredArgsConstructor
public class SimulationController {

    private final OpeningRequestService service;

    @PostMapping
    public ResponseEntity<Void> simulate(@PathVariable Long crateId,
                                         @RequestParam(defaultValue = "1000") int count) {
        service.requestOpenings(crateId, count);
        return ResponseEntity.accepted().build();
    }
}
