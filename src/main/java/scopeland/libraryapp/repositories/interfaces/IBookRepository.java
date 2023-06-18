package scopeland.libraryapp.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import scopeland.libraryapp.entities.Book;

/*
 * A simple repository interface built ontop of a JPA Repository for a Book Entity and its string ISBN Primary key
 */
@Repository
public interface IBookRepository extends JpaRepository<Book, String> {

    /*
     * Custom method to search for books between certain dates
     * 
     * @param startYear - The lower search limit
     * 
     * @param endYear - the upper search limit
     * 
     * @result - The list of books matching the search
     */

    @Query(value = "SELECT * FROM books b WHERE b.publication_year BETWEEN :startYear AND :endYear", nativeQuery = true)
    List<Book> searchBooksInYearRange(
            @Param("startYear") Integer startYear,
            @Param("endYear") Integer endYear);
}