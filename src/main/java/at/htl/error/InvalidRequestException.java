package at.htl.error;

public final class InvalidRequestException extends DomainException {

    public InvalidRequestException(String field, String message) {
        super(field, message);
    }
}
