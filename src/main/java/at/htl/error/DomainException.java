package at.htl.error;

public abstract sealed class DomainException extends RuntimeException
        permits ConflictException, DuplicateException, InvalidRequestException, NotFoundException {

    private final String field;

    protected DomainException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String field() {
        return field;
    }
}
