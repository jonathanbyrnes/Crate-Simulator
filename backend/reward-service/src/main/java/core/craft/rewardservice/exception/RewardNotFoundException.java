package core.craft.rewardservice.exception;

public class RewardNotFoundException extends RuntimeException {
    public RewardNotFoundException(Long id) {
        super("Reward not found with ID: " + id);
    }
}
