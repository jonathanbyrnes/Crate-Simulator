package core.craft.openingservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void handleRewardNotFound() {
        ResponseEntity<?> response = handler.handleRewardNotFound(new RewardNotFoundException(1L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Reward not found with ID: 1"));
    }

    @Test
    public void handleApprovedRewardNotFound() {
        ResponseEntity<?> response = handler.handleApprovedRewardNotFound(new ApprovedRewardNotFoundException(2L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "No approved rewards in crate: 2"));
    }

    @Test
    public void handleRewardForCrateNotFound() {
        ResponseEntity<?> response = handler.handleRewardForCrateNotFound(new RewardForCrateNotFoundException(3L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Rewards for crate: 3 cannot be found."));
    }

    @Test
    public void handleCrateNotApproved() {
        ResponseEntity<?> response = handler.handleCrateNotApproved(new CrateNotApprovedException(4L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Crate is not approved with ID: 4"));
    }
}
