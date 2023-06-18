package scopeland.libraryapp.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import scopeland.libraryapp.entities.User;

public interface IUserRepository extends JpaRepository<User, Long> {

    /*
     * Find user by email
     * 
     * @param email - the users email address
     * 
     * @result - the matching user
     */
    User findByEmail(String email);

}
