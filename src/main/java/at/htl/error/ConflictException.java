package at.htl.error;

public final class ConflictException extends DomainException {

    public ConflictException(String field, String message) {
        super(field, message);
    }
}
