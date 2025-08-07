package core.craft.crateservice.controller;

import core.craft.crateservice.dto.OpeningDto;
import core.craft.crateservice.service.OpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/crates/{crateId}/open")
@RequiredArgsConstructor
public class OpeningController {

    private final OpeningService service;

    @PostMapping
    public ResponseEntity<OpeningDto> open(@PathVariable Long crateId) {
        OpeningDto openingDto = service.open(crateId);
        return ResponseEntity.created(URI.create("/api/openings/" + openingDto.getId()))
                .body(openingDto);
    }

}

