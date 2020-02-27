package inventory.validator;

public interface IValidator<E>  {
    void validate(E e) throws ValidatorException;
}
