package scopeland.libraryapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import scopeland.libraryapp.entities.Role;
import scopeland.libraryapp.entities.User;
import scopeland.libraryapp.entities.dtos.UserDto;
import scopeland.libraryapp.repositories.interfaces.IRoleRepository;
import scopeland.libraryapp.repositories.interfaces.IUserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private IUserRepository userRepository;
    private IRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(IUserRepository userRepository,
            IRoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    /*
     * Saves the user to the database
     * 
     * @param userDto - The user to be saved
     */
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Assign the access role (Only Admin at this point)
        Role role = roleRepository.findByName("ADMIN");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    /*
     * Find a specific user based on their email address
     * 
     * @param - The email we are looking for a user for
     * 
     * @result - the relevant user with the matching email
     */
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /*
     * List all existing users
     * 
     * @result - A list of all the users
     */
    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    /*
     * Mapper from User to UserDto
     * 
     * @param user - the original user object
     * 
     * @returns - The newly mapped dto
     */
    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    /*
     * Check Role Exists (currently on ROLE_ADMIN)
     * 
     * @return The Admin Role that will have just been created if it did not already
     * exist
     */
    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}
