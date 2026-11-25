package at.htl.error;

public final class NotFoundException extends DomainException {

    public NotFoundException(String field, String message) {
        super(field, message);
    }
}
