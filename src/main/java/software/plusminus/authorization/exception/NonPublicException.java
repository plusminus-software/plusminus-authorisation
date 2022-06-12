package software.plusminus.authorization.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NonPublicException extends RuntimeException {

    public NonPublicException() {
        super("User must be authenticated to call non-public apis");
    }
}
