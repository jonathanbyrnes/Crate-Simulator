package core.craft.openingservice.exception;

public class RewardForCrateNotFoundException extends RuntimeException {
    public RewardForCrateNotFoundException(Long id) {
        super("Rewards for crate: " + id + " cannot be found.");
    }
}
