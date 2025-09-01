package core.craft.openingservice.exception;

import java.util.NoSuchElementException;

public class ApprovedRewardNotFoundException extends NoSuchElementException {
    public ApprovedRewardNotFoundException(Long id) {
        super("No approved rewards in crate: " + id);
    }
}
