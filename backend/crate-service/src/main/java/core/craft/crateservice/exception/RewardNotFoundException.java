package core.craft.crateservice.exception;

public class RewardNotFoundException extends RuntimeException {
    public RewardNotFoundException(Long id) {
        super("Reward not found with ID: " + id);
    }
}
