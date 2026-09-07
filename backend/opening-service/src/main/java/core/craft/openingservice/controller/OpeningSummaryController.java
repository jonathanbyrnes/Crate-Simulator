package core.craft.openingservice.controller;

import core.craft.openingservice.dto.OpeningSummaryDto;
import core.craft.openingservice.service.OpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crates/{crateId}/openings")
@RequiredArgsConstructor
public class OpeningSummaryController {

    private final OpeningService service;

    @GetMapping
    public ResponseEntity<OpeningSummaryDto> summarise(@PathVariable Long crateId) {
        return ResponseEntity.ok(service.summarise(crateId));
    }
}
