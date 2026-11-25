package at.htl.error;

public final class DuplicateException extends DomainException {

    public DuplicateException(String field, String message) {
        super(field, message);
    }
}
