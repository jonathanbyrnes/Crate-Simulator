package core.craft.rewardservice.exception;

public class CrateNotApprovedException extends RuntimeException {
    public CrateNotApprovedException(Long id) {
        super("Crate is not approved with ID: " + id);
    }
}
