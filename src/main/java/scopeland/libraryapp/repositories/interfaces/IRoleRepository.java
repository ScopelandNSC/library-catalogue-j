package scopeland.libraryapp.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import scopeland.libraryapp.entities.Role;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    /*
     * Finds a specific role based on its name
     * 
     * @param name - The name of the role
     * 
     * @result - The returned Role
     */
    Role findByName(String name);
}