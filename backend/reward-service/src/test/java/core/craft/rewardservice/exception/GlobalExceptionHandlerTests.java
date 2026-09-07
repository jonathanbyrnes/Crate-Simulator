package core.craft.rewardservice.exception;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void handleRewardNotFound() {
        ResponseEntity<?> response = handler.handleRewardNotFound(new RewardNotFoundException(1L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Reward not found with ID: 1"));
    }

    @Test
    public void handleCrateNotFound() {
        ResponseEntity<?> response = handler.handleCrateNotFound(new CrateNotFoundException(2L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Crate not found with ID: 2"));
    }

    @Test
    public void handleFeignNotFound() {
        ResponseEntity<?> response = handler.handleFeignNotFound(mock(FeignException.NotFound.class));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Crate not found"));
    }

    @Test
    public void handleCrateNotApproved() {
        ResponseEntity<?> response = handler.handleCrateNotApproved(new CrateNotApprovedException(4L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Crate is not approved with ID: 4"));
    }
}
