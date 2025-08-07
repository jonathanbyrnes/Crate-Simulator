package core.craft.crateservice.controller;

import core.craft.crateservice.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crates/{crateId}/simulate")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService service;

    @PostMapping
    public ResponseEntity<Void> simulate(@PathVariable Long crateId,
                                         @RequestParam(defaultValue = "1000") int count) {
        service.simulate(crateId, count);
        return ResponseEntity.accepted().build();
    }
}
