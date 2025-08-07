    package core.craft.crateservice.controller;

    import core.craft.crateservice.dto.CreateOpeningRequest;
    import core.craft.crateservice.dto.OpeningDto;
    import core.craft.crateservice.publisher.OpeningPublisher;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/crates/{crateId}/open")
    @RequiredArgsConstructor
    public class OpeningController {

        private final OpeningPublisher publisher;

        @PostMapping
        public ResponseEntity<OpeningDto> open(@PathVariable Long crateId) {
            publisher.publishOpening(new CreateOpeningRequest(crateId));
            return ResponseEntity.accepted().build();
        }

    }

