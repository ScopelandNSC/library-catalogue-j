package scopeland.libraryapp.validation.books.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import scopeland.libraryapp.validation.books.constraint.IsbnConstraint;

/*
 * Validates an ISBN is valid
 * 
 * This is a very simple ISBN Validator.
 * Since I am not a Java developer I though it would be good experience to write a custom validator
 * ISBNs are not this simple, they have check digits and you could apply a very strict regex to them
 * They should only have spaces or dashes, a maximum of 3 and not a mix of both
 * If I was to actually use an ISBN in a production app I would just use the pre-built validator:
 * https://docs.jboss.org/hibernate/stable/validator/api/org/hibernate/validator/constraints/ISBN.html
 */
public class IsbnValidator implements
        ConstraintValidator<IsbnConstraint, String> {

    @Override
    public void initialize(IsbnConstraint isbn) {
    }

    /*
     * The main method to check if an ISBN is valid.
     * 
     * @param isbn - the isbn being validated
     * 
     * @context - The context we can add validation errors too
     */
    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        var isLengthValid = isLengthValid(isbn);
        var isFormatValid = isFormatValid(isbn);

        if (!isLengthValid) {
            context.buildConstraintViolationWithTemplate("ISBN must be 10 or 13 digits long")
                    .addConstraintViolation();
        }

        if (!isFormatValid) {
            context.buildConstraintViolationWithTemplate("ISBN must only contain numbers, dashes and spaces")
                    .addConstraintViolation();
        }

        return isLengthValid && isFormatValid;
    }

    /*
     * A method to check the length of the isbn is valid
     * 
     * @param isbn - the isbn being validated
     * 
     * @returns - Whether the length is valid or not
     */
    private boolean isLengthValid(String isbn) {
        isbn = isbn.replaceAll("-", "");
        isbn = isbn.replaceAll(" ", "");
        return isbn.length() == 10 || isbn.length() == 13;
    }

    /*
     * A method to check the format of the isbn is valid
     * 
     * @param isbn - the isbn being validated
     * 
     * @returns - Whether the format is valid or not
     */
    private boolean isFormatValid(String isbn) {
        return isbn.matches("^([0-9]|-| )*$");
    }
}