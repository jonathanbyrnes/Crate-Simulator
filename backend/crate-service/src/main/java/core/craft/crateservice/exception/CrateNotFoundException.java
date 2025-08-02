package core.craft.crateservice.exception;

public class CrateNotFoundException extends RuntimeException {
    public CrateNotFoundException(Long id) {
        super("Crate not found with ID: " + id);
    }
}

