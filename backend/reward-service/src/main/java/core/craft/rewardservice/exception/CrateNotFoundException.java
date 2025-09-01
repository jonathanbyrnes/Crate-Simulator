package core.craft.rewardservice.exception;

public class CrateNotFoundException extends RuntimeException {
    public CrateNotFoundException(Long id) {
        super("Crate not found with ID: " + id);
    }
}

