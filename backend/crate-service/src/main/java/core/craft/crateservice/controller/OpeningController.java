package core.craft.crateservice.controller;

import core.craft.crateservice.service.OpeningRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crates/{crateId}/open")
@RequiredArgsConstructor
public class OpeningController {

    private final OpeningRequestService service;

    @PostMapping
    public ResponseEntity<Void> open(@PathVariable Long crateId) {
        service.requestOpening(crateId);
        return ResponseEntity.accepted().build();
    }

}

