package core.craft.openingservice.exception;

public class RewardNotFoundException extends RuntimeException {
    public RewardNotFoundException(Long id) {
        super("Reward not found with ID: " + id);
    }
}
