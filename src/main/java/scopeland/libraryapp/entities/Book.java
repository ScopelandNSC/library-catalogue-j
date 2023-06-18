package scopeland.libraryapp.entities;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import scopeland.libraryapp.enums.BookStatus;
import scopeland.libraryapp.validation.books.constraint.IsbnConstraint;

/*
 * The Book Entity we store in the database
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @NotBlank(message = "ISBN is required")
    @IsbnConstraint
    private String isbn;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotNull(message = "Publication Year is required")
    private Integer publicationYear;

    @NotNull(message = "Book Status is required")
    private BookStatus bookStatus;

    /*
     * An override for the equals method to compare 2 books
     * Returns false if the passed in object is not a Book.
     * 
     * @param o - The object to be compared against the book
     * 
     * return - returns true if equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Book))
            return false;
        Book book = (Book) o;
        return Objects.equals(this.isbn, book.isbn)
                && Objects.equals(this.title, book.title)
                && Objects.equals(this.author, book.author)
                && Objects.equals(this.publicationYear, book.publicationYear)
                && Objects.equals(this.bookStatus, book.bookStatus);

    }

    /*
     * An override for the toString() method to convert a book to a single string
     * 
     * return - returns one long string representing the object and its properties.
     */
    @Override
    public String toString() {
        return "Book {" + "isbn=" + this.isbn + ", title='" + this.title + '\'' + ", author='" + this.author + '\''
                + ", publicationYear='" + this.publicationYear.toString() + '\'' + ", bookStatus='"
                + this.bookStatus.toString() + '\''
                + '}';
    }

    /*
     * a method to use a library to create a good json copy of the obhect.
     * Very similar to toString.
     * Required for testing
     */
    public String mapToJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
