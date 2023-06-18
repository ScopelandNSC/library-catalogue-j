package scopeland.libraryapp.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import scopeland.libraryapp.entities.Role;
import scopeland.libraryapp.entities.User;
import scopeland.libraryapp.repositories.interfaces.IUserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private IUserRepository userRepository;

    public CustomerUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * A method that loads the user based on their username, and throws an error if
     * the user can't be found
     * 
     * @param email - the username of the user (their email)
     * 
     * @return - The details of the associated usr
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRoles()));
        } else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    /*
     * A method that links a specific role to the authorisations they have
     * 
     * @param roles - A list of roles
     * 
     * @result - A collection of all the authorities for these roles
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return mapRoles;
    }
}
