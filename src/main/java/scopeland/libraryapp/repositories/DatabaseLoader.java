package scopeland.libraryapp.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import scopeland.libraryapp.entities.Book;
import scopeland.libraryapp.enums.BookStatus;
import scopeland.libraryapp.repositories.interfaces.IBookRepository;
import scopeland.libraryapp.repositories.interfaces.IRoleRepository;
import scopeland.libraryapp.repositories.interfaces.IUserRepository;

/*
 * A little component more for local testing that preloads some database data.
 * 
 * If this was a real work production project I would be unlikely to include this 
 * and manage our database through an external tool such as liquibase.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final IBookRepository bookRepository;

    @Autowired
    public DatabaseLoader(IBookRepository bookRepository, IRoleRepository roleRepository,
            IUserRepository userRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        bookRepository.save(new Book("9781473619791", "The Long Way to a Small Angry Planet", "Becky Chambers", 2014,
                BookStatus.AVAILABLE));
        bookRepository.save(
                new Book("9780062959041", "To be Taught if Fortunate", "Becky Chambers", 2018, BookStatus.AVAILABLE));
        bookRepository.save(new Book("9780062877239", "Chilling Effect", "Valerie Valdes", 2019, BookStatus.BORROWED));
    }
}