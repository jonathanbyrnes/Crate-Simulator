package core.craft.crateservice.controller;

import core.craft.crateservice.dto.CrateDto;
import core.craft.crateservice.dto.CreateCrateRequest;
import core.craft.crateservice.service.CrateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/crates")
@RequiredArgsConstructor
public class CrateController {

    private final CrateService service;

    @PostMapping
    public ResponseEntity<CrateDto> create(@Validated @RequestBody CreateCrateRequest request) {
        CrateDto crateDto = service.create(request);
        return ResponseEntity.created(URI.create("/api/crates" + crateDto.getId()))
                .body(crateDto);
    }

    @GetMapping
    public ResponseEntity<List<CrateDto>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{crateId}")
    public ResponseEntity<CrateDto> get(@PathVariable Long crateId) {
        return ResponseEntity.ok(service.findById(crateId));
    }

    @PutMapping("/{crateId}")
    public ResponseEntity<CrateDto> update(@PathVariable Long crateId,
                                           @RequestBody CreateCrateRequest request) {
        return ResponseEntity.ok(service.update(crateId, request));
    }

    @DeleteMapping("/{crateId}")
    public ResponseEntity<Void> delete(@PathVariable Long crateId) {
        service.delete(crateId);
        return ResponseEntity.noContent().build();
    }
}
